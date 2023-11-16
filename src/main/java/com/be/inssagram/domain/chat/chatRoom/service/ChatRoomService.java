package com.be.inssagram.domain.chat.chatRoom.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.chat.chatRoom.dto.request.ChatRoomRequest;
import com.be.inssagram.domain.chat.chatRoom.dto.response.ChatRoomResponse;
import com.be.inssagram.domain.chat.chatRoom.dto.response.ChatRoomResponse2;
import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import com.be.inssagram.domain.chat.chatRoom.repository.ChatRoomRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

//    private final TokenProvider tokenProvider;

    @Transactional
    public ChatRoomResponse2 createChatRoom(ChatRoomRequest request) {
        Long firstId;
        Long secondId;
        if (request.getFirstParticipantId() > request.getSecondParticipantId()) {
            firstId = request.getSecondParticipantId();
            secondId = request.getFirstParticipantId();
        } else {
            firstId = request.getFirstParticipantId();
            secondId = request.getSecondParticipantId();
        }

        // 방 있다면 찾아서 넘겨줌.
        if (chatRoomRepository.findByFirstParticipantIdAndSecondParticipantId(
                firstId, secondId) != null) {
            return ChatRoomResponse2.from(chatRoomRepository
                    .findByFirstParticipantIdAndSecondParticipantId(
                            firstId, secondId));
        }

        // 방 없으면 방 생성.
        Member firstParticipant = memberRepository.findById(firstId)
                .orElseThrow(UserDoesNotExistException::new);
        Member secondParticipant = memberRepository.findById(secondId)
                .orElseThrow(UserDoesNotExistException::new);

        return ChatRoomResponse2.from(chatRoomRepository.save(ChatRoom.builder()
                .firstParticipant(firstParticipant)
                .secondParticipant(secondParticipant)
                .build()));
    }

    @Transactional
    public ChatRoomResponse2 findRoomById(Long roomId) {
        return ChatRoomResponse2.from(chatRoomRepository.findByRoomId(roomId));
    }

    @Transactional
    public List<ChatRoomResponse2> searchRoomsWithMemberId(Long memberId) {
        return chatRoomRepository
                .findByFirstParticipantIdOrSecondParticipantIdOrderByUpdatedAtDesc(
                memberId, memberId).stream().map(ChatRoomResponse2::from).toList();
    }

//    @Transactional
//    public ChatRoomResponse createChatRoom2(String token, ChatRoomRequest request) {
//        Member first;
//        Member second;
//        Member sender = tokenProvider.getMemberFromToken(token);
//        Member receiver = memberRepository.findById(request.getSecondParticipantId())
//                .orElseThrow(UserDoesNotExistException::new);
//        if (sender.getId() > request.getSecondParticipantId()) {
//            first = receiver;
//            second = sender;
//        } else {
//            first = sender;
//            second = receiver;
//        }
//
//        // 방 있다면 찾아서 넘겨줌.
//        if (chatRoomRepository.findByFirstParticipantIdAndSecondParticipantId(
//                first.getId(), second.getId()) != null) {
//
//            if (first.equals(sender)) {
//                return ChatRoomResponse.from(chatRoomRepository
//                        .findByFirstParticipantIdAndSecondParticipantId(
//                                sender.getId(), receiver.getId()));
//            } else {
//                Long chatRoomId = chatRoomRepository
//                        .findByFirstParticipantIdAndSecondParticipantId(
//                                first.getId(), second.getId()).getRoomId();
//                return ChatRoomResponse.createResponse(
//                        chatRoomId, sender, receiver);
//            }
//
//        }
//
//        // 방 없으면 방 생성.
//        if (first.equals(sender)) {
//            return ChatRoomResponse.from(chatRoomRepository.save(ChatRoom.builder()
//                    .firstParticipant(first).secondParticipant(second).build()));
//        } else {
//            Long chatRoomId = chatRoomRepository.save(ChatRoom.builder()
//                            .firstParticipant(first).secondParticipant(second).build())
//                    .getRoomId();
//
//            return ChatRoomResponse.createResponse(
//                    chatRoomId, sender, receiver);
//        }
//    }
//
//    @Transactional
//    public ChatRoomResponse findRoomById2(String token, Long roomId) {
//        Member sender = tokenProvider.getMemberFromToken(token);
//        if (sender.equals(chatRoomRepository
//                .findByRoomId(roomId).getFirstParticipant())) {
//            return ChatRoomResponse.from(chatRoomRepository.findByRoomId(roomId));
//        } else {
//            return ChatRoomResponse.createResponse(roomId, sender,
//                    chatRoomRepository.findByRoomId(roomId)
//                            .getFirstParticipant());
//        }
//
//    }
//
//    @Transactional
//    public List<ChatRoomResponse> searchRoomsWithMemberId2(Long memberId) {
//        List<ChatRoomResponse> responses = new ArrayList<>();
//
//        for (ChatRoom chatRoom : chatRoomRepository
//                .findByFirstParticipantIdOrSecondParticipantIdOrderByUpdatedAtDesc(
//                memberId, memberId)) {
//
//            if (chatRoom.getFirstParticipant().getId().equals(memberId)) {
//                responses.add(ChatRoomResponse.from(chatRoom));
//            } else {
//                responses.add(ChatRoomResponse.createResponse(
//                        chatRoom.getRoomId()
//                        , chatRoom.getSecondParticipant()
//                        , chatRoom.getFirstParticipant()));
//            }
//        }
//
//        return responses;
//    }

    @Transactional
    public ChatRoomResponse2 findByFirstParticipantIdAndSecondParticipantId(
            Long firstMemberId, Long secondMemberId
    ) {
        return ChatRoomResponse2.from(chatRoomRepository
                .findByFirstParticipantIdAndSecondParticipantId(
                        firstMemberId, secondMemberId));
    }

    public List<ChatRoom> searchRooms() {
        return chatRoomRepository.findAll();
    }

}
