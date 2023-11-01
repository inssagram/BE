package com.be.inssagram.domain.notification.service;


import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.notification.dto.NotificationRequest;
import com.be.inssagram.domain.notification.entity.Notification;
import com.be.inssagram.domain.notification.repository.EmittersRepository;
import com.be.inssagram.domain.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
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

        sendToClient(userId, "EventStream Created. [userId=" + userId + "]");
        return emitter;
    }

    //알람 전송
    public void notify(NotificationRequest request) {
        sendToClient(request.getReceiver_id(), request.getMessage());
        notificationRepository.save(saveNotification(request));
    }

    //알림 삭제
    public void deleteNotification(Long member_id, Long notification_id) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notification_id, member_id);
        notificationRepository.delete(notification);
    }

    //알림 조회후 안읽은 알림은 읽음 상태로 변환
    public List<Notification> getMyNotifications(Long member_id) {
        List<Notification> list = notificationRepository.findAllByReceiverIdAndReadStatus(member_id, false);
        for (Notification notification : list) {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
        return notificationRepository.findByReceiverId(member_id);
    }

    //알림 생성
    public NotificationRequest createNotifyDto(Long receiver_id, String location, Long location_id, Long sender_id,
                                               String senderName, String senderImage, String message){
        return NotificationRequest.builder()
                .receiver_id(receiver_id)
                .location(location)
                .location_id(location_id)
                .sender_id(sender_id)
                .senderName(senderName)
                .senderImage(senderImage)
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
                emitter.send(SseEmitter.event().id(String.valueOf(id)).name("sse").data(data));
            } catch (IOException exception) {
                emittersRepository.deleteById(id);
                emitter.completeWithError(exception);
            }
        }
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
        Follow exists = followRepository.findByMyIdAndMemberId(request.getSender_id(), request.getReceiver_id());
        boolean isFriend = false;
        if(exists != null){
            List<Notification> list = notificationRepository
                    .findAllByReceiverIdAndSenderId(request.getReceiver_id(), request.getSender_id());
            for (Notification notification : list) {
                notification.setFriendStatus(true);
                notificationRepository.save(notification);
            }
            isFriend = true;
        }
        return Notification.builder()
                .message(request.getMessage())
                .senderId(request.getSender_id())
                .senderImage(request.getSenderImage())
                .senderName(request.getSenderName())
                .location(request.getLocation())
                .location_id(request.getLocation_id())
                .friendStatus(isFriend)
                .readStatus(false)
                .createdAt(LocalDateTime.now())
                .receiverId(request.getReceiver_id())
                .build();
    }
}
