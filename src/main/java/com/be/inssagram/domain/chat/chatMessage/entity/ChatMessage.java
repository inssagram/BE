package com.be.inssagram.domain.chat.chatMessage.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ChatMessage")
public class ChatMessage extends BaseEntity {
    // 메시지 타입 : 입장, 채팅

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChatMessage_ID")
    private Long id;

    private Long roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지

    @ManyToOne
    private Post post;

}
