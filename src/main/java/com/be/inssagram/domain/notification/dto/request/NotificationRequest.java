package com.be.inssagram.domain.notification.dto.request;


import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.post.entity.Post;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Member receiver_info;
    private Post post_info;
    private Member sender_info;
    private String message;
    private Long chatroom_id;
}
