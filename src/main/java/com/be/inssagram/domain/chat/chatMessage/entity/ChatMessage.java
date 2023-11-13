package com.be.inssagram.domain.chat.chatMessage.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.chat.ChatMessageType;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.stroy.entity.Story;
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
    private Long roomId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;          // 메시지 보낸 사람
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;        // 메시지 받는 사람
    @Enumerated(EnumType.STRING)
    private ChatMessageType type;   // 메세지 타입
    private String message;         // 메시지
    private String image;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Story story;

}
