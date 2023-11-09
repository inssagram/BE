package com.be.inssagram.domain.chat.chatMessage.dto.response;

import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageWithPostResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private String sender;
    private String message;
    private String createdAt;
    private Long postId;
    private String postImage;
    private String postContents;
    private Long memberIdInPost;
    private String memberProfileInPost;
    private String memberNicknameInPost;


    public static ChatMessageWithPostResponse from(ChatMessage message) {
        String postImage = "";
        if (message.getPost().getImage() != null) {
            postImage = message.getPost().getImage().get(0);
        }
        return ChatMessageWithPostResponse.builder()
                .chatMessageId(message.getId())
                .chatRoomId(message.getRoomId())
                .sender(message.getSender())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .postId(message.getPost().getId())
                .postImage(postImage)
                .postContents(message.getPost().getContents())
                .memberIdInPost(message.getPost().getMember().getId())
                .memberNicknameInPost(message.getPost().getMember().getNickname())
                .build();
    }
}
