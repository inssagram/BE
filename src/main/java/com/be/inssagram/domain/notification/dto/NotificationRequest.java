package com.be.inssagram.domain.notification.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NotificationRequest {
    private Long receiver_id;
    private Long location_id;
    private Long sender_id;
    private String location;
    private String senderName;
    private String senderImage;
    private String message;
}
