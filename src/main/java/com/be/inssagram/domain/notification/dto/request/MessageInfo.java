package com.be.inssagram.domain.notification.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageInfo {
    private String message;
    private Long chatroomId;
    private Integer unreadCount;
    private Integer unreadChatCount;
}
