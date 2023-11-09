package com.be.inssagram.domain.chat.chatMessage.dto.response;

import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageOnlyResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private String sender;
    private String message;
    private String createdAt;

    public static ChatMessageOnlyResponse from(ChatMessage message) {
        return ChatMessageOnlyResponse.builder()
                .chatMessageId(message.getId())
                .chatRoomId(message.getRoomId())
                .sender(message.getSender())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
