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
public class ChatMessageWithStoryResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private String sender;
    private Long senderMemberId;
    private String senderProfile;
    private String receiver;
    private String message;
    @Enumerated(EnumType.STRING)
    private ChatMessageType type;
    private String createdAt;
    private Long storyId;
    private String storyImage;
    private Long memberIdInStory;
    private String memberProfileInStory;
    private String memberNicknameInStory;


    public static ChatMessageWithStoryResponse from(ChatMessage message) {
        return ChatMessageWithStoryResponse.builder()
                .chatMessageId(message.getId())
                .chatRoomId(message.getRoomId())
                .sender(message.getSender().getNickname())
                .senderMemberId(message.getSender().getId())
                .senderProfile(message.getSender().getImage())
                .receiver(message.getReceiver().getNickname())
                .type(message.getType())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .storyId(message.getStory().getId())
                .storyImage(message.getStory().getImages())
                .memberIdInStory(message.getStory().getMember().getId())
                .memberProfileInStory(message.getStory().getMember().getImage())
                .memberNicknameInStory(message.getStory().getMember().getNickname())
                .build();
    }
}
