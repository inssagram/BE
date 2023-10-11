package com.be.inssagram.domain.like.entity;

import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.post.entity.Post;
import jakarta.annotation.Nullable;
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
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private Comment comment;

}
