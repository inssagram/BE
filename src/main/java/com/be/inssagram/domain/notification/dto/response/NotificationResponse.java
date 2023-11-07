package com.be.inssagram.domain.notification.dto.response;

import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.entity.Notification;
import lombok.Builder;


@Builder
public record NotificationResponse(
        Long id,
        String created_at,
        boolean friend_status,
        boolean read_status,
        Long post_id,
        String post_image,
        String message,
        Long sender_id,
        String sender_image,
        String sender_name
) {
    public static NotificationResponse fromEntity(Notification notification) {
        if (notification.getPostInfo() == null) {
            return followNotification(notification);
        }
            return NotificationResponse.builder()
                    .id(notification.getId())
                    .created_at(notification.getCreatedAt())
                    .friend_status(notification.isFriendStatus())
                    .read_status(notification.isReadStatus())
                    .post_id(notification.getPostInfo().getId())
                    .post_image(notification.getPostInfo().getImage().get(0))
                    .message(notification.getMessage())
                    .sender_id(notification.getSenderInfo().getId())
                    .sender_image(notification.getSenderInfo().getImage())
                    .sender_name(notification.getSenderInfo().getNickname())
                    .build();
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
}

