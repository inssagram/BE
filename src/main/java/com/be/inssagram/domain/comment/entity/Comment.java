package com.be.inssagram.domain.comment.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.comment.dto.request.CommentRequest;
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
//@SQLDelete(sql = "UPDATE inssagram.comment SET deleted_at = current_timestamp WHERE comment_id = ?")
//@Where(clause = "deleted_at is NULL")
@Entity(name = "COMMENT")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 댓글은 하나의 Post에 속합니다.
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 대댓글의 부모 댓글
    @JoinColumn
    private Comment parentComment; // 부모 댓글

    // 대댓글을 작성할 수 있는 필드
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    // 댓글과 대댓글을 구분하기 위한 필드
    private boolean replyFlag;

    //멘션할 사람을 담기 위한 필드
    private List<String> mentionList;

    public void updateFields(CommentRequest request) {
        if (request.getContents() != null) {
            content = request.getContents();
        }
        if (request.getMentionList() != null) {
            mentionList = request.getMentionList();
        }
    }

}