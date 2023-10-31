package com.be.inssagram.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    List<ChatRoom> findAll();

    ChatRoom findByRoomId(Long roomId);

}