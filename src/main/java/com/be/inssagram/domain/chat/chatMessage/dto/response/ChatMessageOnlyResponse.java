package com.be.inssagram.domain.chat.chatMessage.dto.response;

import com.be.inssagram.domain.chat.ChatMessageType;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String senderNickname;
    private Long senderMemberId;
    private String senderProfile;
    private String receiverNickname;
    private String message;
    @Enumerated(EnumType.STRING)
    private ChatMessageType type;
    private String image;
    private String createdAt;

    public static ChatMessageOnlyResponse from(ChatMessage message) {
        return ChatMessageOnlyResponse.builder()
                .chatMessageId(message.getId())
                .chatRoomId(message.getRoomId())
                .senderNickname(message.getSender().getNickname())
                .senderMemberId(message.getSender().getId())
                .senderProfile(message.getSender().getImage())
                .receiverNickname(message.getReceiver().getNickname())
                .type(message.getType())
                .message(message.getMessage())
                .image(message.getImage())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
