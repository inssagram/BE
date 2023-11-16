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
public class ChatMessageResponse {

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
    private PostType sharePostType;
    private Long shareObjectId;
    private String shareObjectImage;
    private String shareObjectContents;
    private Long memberIdInShareObject;
    private String memberProfileInShareObject;
    private String memberNicknameInShareObject;

    public static ChatMessageResponse from(ChatMessage message) {
        ChatMessageResponseBuilder builder = ChatMessageResponse.builder()
                .chatMessageId(message.getId())
                .chatRoomId(message.getRoomId())
                .senderNickname(message.getSender().getNickname())
                .senderMemberId(message.getSender().getId())
                .senderProfile(message.getSender().getImage())
                .receiverNickname(message.getReceiver().getNickname())
                .type(message.getType())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt());

        switch (message.getType()) {
            case messageWithStory -> {
                return builder
                        .shareObjectId(message.getStory().getId())
                        .shareObjectImage(message.getStory().getImages())
                        .memberIdInShareObject(message.getStory()
                                .getMember().getId())
                        .memberProfileInShareObject(message.getStory()
                                .getMember().getImage())
                        .memberNicknameInShareObject(message.getStory()
                                .getMember().getNickname())
                        .build();
            }
            case messageWithPost -> {
                String postImage = message.getPost().getImage() != null ?
                        message.getPost().getImage().get(0) : "";
                return builder
                        .sharePostType(PostType.post)
                        .shareObjectId(message.getPost().getId())
                        .shareObjectImage(postImage)
                        .shareObjectContents(message.getPost().getContents())
                        .memberIdInShareObject(message.getPost()
                                .getMember().getId())
                        .memberNicknameInShareObject(message.getPost()
                                .getMember().getNickname())
                        .memberProfileInShareObject(message.getPost()
                                .getMember().getImage())
                        .build();
            }
            default -> {
                return builder.image(message.getImage()).build();
            }
        }
    }
}
