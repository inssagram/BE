package com.be.inssagram.domain.chat.chatRoom.repository;

import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    List<ChatRoom> findAll();

    ChatRoom findByRoomId(Long roomId);

    ChatRoom findByFirstParticipantIdAndSecondParticipantId(
            Long firstMemberId, Long secondMemberId);

    List<ChatRoom> findByFirstParticipantIdOrSecondParticipantId(
            Long memberId, Long memberId2);

}