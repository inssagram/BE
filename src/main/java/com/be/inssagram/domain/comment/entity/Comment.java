package com.be.inssagram.domain.comment.entity;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE inssagram.comment SET deleted_at = current_timestamp WHERE comment_id = ?")
@Where(clause = "deleted_at is NULL")
@Entity(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne // 댓글은 하나의 Post에 속합니다.
    @JoinColumn()
    private Post post;
    @ManyToOne
    private Member member;
    private String content;

    private Long level;
    private Long order;
    private Long groupNumber;
}
