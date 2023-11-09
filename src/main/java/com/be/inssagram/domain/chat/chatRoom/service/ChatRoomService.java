package com.be.inssagram.domain.chat.chatRoom.service;

import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import com.be.inssagram.domain.chat.chatRoom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String name) {
        return chatRoomRepository.save(ChatRoom.builder().name(name).build());
    }

    public ChatRoom findRoomById(Long roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    public List<ChatRoom> searchRooms() {
        return chatRoomRepository.findAll();
    }

}
