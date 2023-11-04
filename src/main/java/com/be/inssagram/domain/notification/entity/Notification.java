package com.be.inssagram.domain.notification.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;
    private String location;
    private Long location_id;
    private String postImage;
    private Long senderId;
    private String senderName;
    private String senderImage;
    private String message;
    private boolean friendStatus;
    private boolean readStatus;
    private String createdAt;
}
