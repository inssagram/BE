package com.be.inssagram.domain.like.entity;

import com.be.inssagram.domain.like.type.LikeType;
import com.be.inssagram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Enumerated(EnumType.STRING)
    private LikeType likeType;
    private Long likeTypeId;

}
