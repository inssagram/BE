package com.be.inssagram.domain.chat.chatRoom.service;

import com.be.inssagram.domain.chat.chatRoom.dto.request.ChatRoomRequest;
import com.be.inssagram.domain.chat.chatRoom.dto.response.ChatRoomResponse;
import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import com.be.inssagram.domain.chat.chatRoom.repository.ChatRoomRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest request) {
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
            return ChatRoomResponse.from(chatRoomRepository
                    .findByFirstParticipantIdAndSecondParticipantId(
                            firstId, secondId));
        }

        // 방 없으면 방 생성.
        Member firstParticipant = memberRepository.findById(firstId)
                .orElseThrow(UserDoesNotExistException::new);
        Member secondParticipant = memberRepository.findById(secondId)
                .orElseThrow(UserDoesNotExistException::new);

        return ChatRoomResponse.from(chatRoomRepository.save(ChatRoom.builder()
                .firstParticipant(firstParticipant)
                .secondParticipant(secondParticipant)
                .build()));
    }

    @Transactional
    public ChatRoomResponse findRoomById(Long roomId) {
        return ChatRoomResponse.from(chatRoomRepository.findByRoomId(roomId));
    }

    @Transactional
    public ChatRoomResponse findByFirstParticipantIdAndSecondParticipantId(
            Long firstMemberId, Long secondMemberId
    ) {
        return ChatRoomResponse.from(chatRoomRepository
                .findByFirstParticipantIdAndSecondParticipantId(
                firstMemberId, secondMemberId));
    }

    @Transactional
    public List<ChatRoomResponse> searchRoomsWithMemberId(Long memberId) {
        return chatRoomRepository.findByFirstParticipantIdOrSecondParticipantIdOrderByUpdatedAtDesc(
                memberId, memberId).stream().map(ChatRoomResponse::from).toList();
    }

    public List<ChatRoom> searchRooms() {
        return chatRoomRepository.findAll();
    }

}
