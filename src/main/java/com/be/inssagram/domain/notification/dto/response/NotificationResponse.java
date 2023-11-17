package com.be.inssagram.domain.notification.dto.response;

import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.entity.Notification;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Optional;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationResponse(
        Long id,
        Long list_id,
        String created_at,
        boolean friend_status,
        boolean read_status,
        Long post_id,
        String post_image,
        String message,
        Long sender_id,
        String sender_image,
        String sender_name,
        Long chatroom_id
) {
    public static NotificationResponse fromEntity(Notification notification) {
        return (notification.getPostInfo() == null && notification.getChatroomId() == null)
                ? followNotification(notification)
                : (notification.getChatroomId() != null)
                ? chatroomList(notification)
                : buildWithNonNullValues(notification);
    }

    private static NotificationResponse followNotification(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .created_at(notification.getCreatedAt())
                .friend_status(notification.isFriendStatus())
                .read_status(notification.isReadStatus())
                .post_id(null)
                .post_image(null)
                .message(notification.getMessage())
                .sender_id(notification.getSenderInfo().getId())
                .sender_image(notification.getSenderInfo().getImage())
                .sender_name(notification.getSenderInfo().getNickname())
                .build();
    }

    private static NotificationResponse chatroomList(Notification notification) {
        return NotificationResponse.builder()
                .list_id(notification.getId())
                .created_at(notification.getCreatedAt())
                .friend_status(notification.isFriendStatus())
                .read_status(notification.isReadStatus())
                .chatroom_id(notification.getChatroomId())
                .message(notification.getMessage())
                .sender_id(notification.getSenderInfo().getId())
                .sender_image(notification.getSenderInfo().getImage())
                .sender_name(notification.getSenderInfo().getNickname())
                .build();
    }

    private static NotificationResponse buildWithNonNullValues(Notification notification) {
        NotificationResponseBuilder builder = NotificationResponse.builder()
                .id(notification.getId())
                .created_at(notification.getCreatedAt())
                .friend_status(notification.isFriendStatus())
                .read_status(notification.isReadStatus())
                .message(notification.getMessage())
                .sender_id(notification.getSenderInfo().getId())
                .sender_image(notification.getSenderInfo().getImage())
                .sender_name(notification.getSenderInfo().getNickname());

        if (notification.getPostInfo() != null) {
            builder.post_id(notification.getPostInfo().getId());
            builder.post_image(notification.getPostInfo().getImage().get(0));
        }

        return builder.build();
    }
}

