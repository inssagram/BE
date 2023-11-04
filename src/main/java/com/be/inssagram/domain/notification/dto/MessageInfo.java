package com.be.inssagram.domain.notification.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageInfo {
    private String message;
    private int unreadCount;
}
