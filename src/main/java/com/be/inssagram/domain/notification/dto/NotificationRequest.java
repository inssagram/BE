package com.be.inssagram.domain.notification.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long receiver_id;
    private Long location_id;
    private Long sender_id;
    private String postImage;
    private String location;
    private String senderName;
    private String senderImage;
    private String message;
}
