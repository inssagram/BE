package com.be.inssagram.domain.chat.chatMessage.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {

    private enum type {
        enter, message, messageWithPost
    }
    private type type;
    private Long chatMessageId;
    private Long chatRoomId;
    private String sender;
    private String message;
    private Long postId;
}
