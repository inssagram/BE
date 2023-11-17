package com.be.inssagram.domain.notification.service;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.dto.request.MessageInfo;
import com.be.inssagram.domain.notification.dto.request.NotificationRequest;
import com.be.inssagram.domain.notification.dto.response.NotificationResponse;
import com.be.inssagram.domain.notification.entity.Notification;
import com.be.inssagram.domain.notification.repository.EmittersRepository;
import com.be.inssagram.domain.notification.repository.NotificationRepository;
import com.be.inssagram.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmittersRepository emittersRepository;
    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param userId - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);
        List<Notification> list = notificationRepository.findAllByReceiverIdAndReadStatus(userId);
        List<Notification> chatList = notificationRepository.findUnreadChatMessages(userId);
        sendToClient(userId, setEnterMessage("연결이 되었습니다", list.size(), chatList.size()));
        return emitter;
    }

    //알람 전송
    public void notify(NotificationRequest request) {
        notificationRepository.save(saveNotification(request));
        //안읽은 메세지 갯수 전송
        if(request.getChatroom_id() == null) {
            List<Notification> list = notificationRepository
                    .findAllByReceiverIdAndReadStatus(request.getReceiver_info().getId());
            sendToClient(request.getReceiver_info().getId(), setMessage(request.getMessage(),
                    list.size(), null, null));
        }else{
            List<Notification> chatList = notificationRepository
                    .findUnreadChatMessages(request.getReceiver_info().getId());

            sendToClient(request.getReceiver_info().getId(), setMessage(request.getMessage(),
                    null, request.getChatroom_id(), chatList.size()));
        }
    }

    //알림 삭제
    public void deleteNotification(Long member_id, Long notification_id) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notification_id, member_id);
        notificationRepository.delete(notification);
    }

    //알림 조회후 안읽은 알림은 읽음 상태로 변환
    public List<NotificationResponse> getMyNotifications(Long member_id) {
        List<Notification> list = notificationRepository.findAllByReceiverId(member_id);
        for (Notification notification : list) {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
        List<NotificationResponse> responseList = new ArrayList<>();

        for (Notification notification : list) {
            NotificationResponse response = NotificationResponse.fromEntity(notification);
            responseList.add(response);
        }

        return responseList;
    }

    //알림정보 생성
    public NotificationRequest createNotifyDto(Member receiver_info, Post post_info, Member sender_info,
                                               String message, Long chatroom_id){
        if(chatroom_id == null) {
            return NotificationRequest.builder()
                    .receiver_info(receiver_info)
                    .post_info(post_info)
                    .sender_info(sender_info)
                    .message(message)
                    .build();
        }
        return NotificationRequest.builder()
                .receiver_info(receiver_info)
                .chatroom_id(chatroom_id)
                .sender_info(sender_info)
                .message(message)
                .build();
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param id   - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     */
    private void sendToClient(Long id, Object data) {
        SseEmitter emitter = emittersRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(id)).name("sse")
                        .data(data, MediaType.APPLICATION_JSON));
            } catch (IOException exception) {
                emittersRepository.deleteById(id);
                emitter.completeWithError(exception);
            }
        }
    }

    private MessageInfo setMessage(String msg, Integer unreadCount, Long chatroomId, Integer unreadChatCount){
        if(chatroomId == null) {
            return MessageInfo.builder()
                    .message(msg)
                    .unreadCount(unreadCount)
                    .build();
        }
        return MessageInfo.builder()
                .message(msg)
                .unreadChatCount(unreadChatCount)
                .build();
    }

    private MessageInfo setEnterMessage(String msg, Integer unreadCount, Integer unreadChatCount){
        return MessageInfo.builder()
                .message(msg)
                .unreadCount(unreadCount)
                .unreadChatCount(unreadChatCount)
                .build();
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param id - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emittersRepository.save(id, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emittersRepository.deleteById(id));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emittersRepository.deleteById(id));

        return emitter;
    }

    private Notification saveNotification(NotificationRequest request) {
        Follow exists = followRepository.findByRequesterInfoAndFollowingInfo(request.getSender_info(), request.getReceiver_info());

        boolean isFriend = false;

        if (exists != null) {
            List<Notification> notifications = notificationRepository
                    .findAllByReceiverIdAndSenderInfo(request.getReceiver_info().getId(), request.getSender_info());

            for (Notification notification : notifications) {
                notification.setFriendStatus(true);
            }

            try {
                notificationRepository.saveAll(notifications);
            } catch (Exception e) {
                ApiResponse.createError(e.getMessage());
            }

            isFriend = true;
        }

        // 이미 채팅 알림이 있는지 확인
        if (request.getChatroom_id() != null) {
            Notification existingNotification = notificationRepository.findByChatroomId(request.getChatroom_id());

            if (existingNotification != null) {
                // 존재하는 채팅 알람 업데이트
                existingNotification.setMessage(request.getMessage());
                existingNotification.setReadStatus(false);
                existingNotification.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

                try {
                    notificationRepository.save(existingNotification);
                } catch (Exception e) {
                    ApiResponse.createError(e.getMessage());
                }

                return existingNotification;
            }
        }

        // Create a new notification
        return buildNotification(request, isFriend);
    }

    private Notification buildNotification(NotificationRequest request, boolean isFriend) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        Notification.NotificationBuilder builder = Notification.builder()
                .message(request.getMessage())
                .senderInfo(request.getSender_info())
                .friendStatus(isFriend)
                .readStatus(false)
                .createdAt(formattedDateTime)
                .receiverId(request.getReceiver_info().getId());

        if (request.getChatroom_id() != null) {
            builder.chatroomId(request.getChatroom_id());
        } else {
            builder.postInfo(request.getPost_info());
        }
        return builder.build();
    }
}
