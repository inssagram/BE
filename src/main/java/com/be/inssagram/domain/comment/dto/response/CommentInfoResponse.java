package com.be.inssagram.domain.comment.dto.response;

import com.be.inssagram.domain.comment.entity.Comment;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInfoResponse {

    private Long commentId;
    private Long postId;
    private Long memberId;
    private String content;
    private List<Comment> childComments;
    private boolean replyFlag;

    public CommentInfoResponse(Long commentId, Long postId, Long memberId,
                               String content, boolean replyFlag) {
        this.commentId = commentId;
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
        this.replyFlag = replyFlag;
    }

    public static CommentInfoResponse from(Comment comment) {

        return CommentInfoResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .childComments(comment.getChildComments())
                .replyFlag(comment.isReplyFlag())
                .build();

    }

}
