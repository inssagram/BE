package com.be.inssagram.domain.chat.chatRoom.entity;

import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ChatRoom")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member firstParticipant;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member secondParticipant;
    private String updatedAt;
    @OneToOne
    private ChatMessage lateMessage;

}
