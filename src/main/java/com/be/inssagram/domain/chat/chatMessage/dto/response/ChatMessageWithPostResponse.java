package com.be.inssagram.domain.chat.chatMessage.dto.response;

import com.be.inssagram.domain.chat.ChatMessageType;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.post.type.PostType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String senderNickname;
    private Long senderMemberId;
    private String senderProfile;
    private String receiverNickname;
    private String message;
    @Enumerated(EnumType.STRING)
    private ChatMessageType type;
    private String createdAt;
    @Enumerated(EnumType.STRING)
    private PostType postType;
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
                .senderNickname(message.getSender().getNickname())
                .senderMemberId(message.getSender().getId())
                .senderProfile(message.getSender().getImage())
                .receiverNickname(message.getReceiver().getNickname())
                .type(message.getType())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .postType(PostType.post)
                .postId(message.getPost().getId())
                .postImage(postImage)
                .postContents(message.getPost().getContents())
                .memberIdInPost(message.getPost().getMember().getId())
                .memberNicknameInPost(message.getPost().getMember().getNickname())
                .build();
    }
}
