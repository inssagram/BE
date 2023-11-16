package com.be.inssagram.domain.chat.chatMessage.dto.request;

import com.be.inssagram.domain.chat.ChatMessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    @Enumerated(EnumType.STRING)
    private ChatMessageType type;
    private Long receiverMemberId;
    private String message;
    private String imageUrl;
    private Long postId;
    private Long storyId;
}
