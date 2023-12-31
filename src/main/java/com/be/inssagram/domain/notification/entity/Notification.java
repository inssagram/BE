package com.be.inssagram.domain.notification.entity;


import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_Info")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post postInfo;

    private Long chatroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_Info")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member senderInfo;

    private String message;
    private boolean friendStatus;
    private boolean readStatus;
    private String createdAt;
}
