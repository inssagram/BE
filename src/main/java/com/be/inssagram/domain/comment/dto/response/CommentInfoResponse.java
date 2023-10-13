package com.be.inssagram.domain.comment.dto.response;

import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.like.dto.response.LikeInfoResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
//    private List<ReplyInfoResponse> childComments;
    private Integer CommentCount;
//    private Set<LikeInfoResponse> likedByPerson;
    private Integer likeCount;
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
        List<ReplyInfoResponse> list = new ArrayList<>();
        if (comment.getChildComments() != null) {
            list = comment.getChildComments().stream()
                    .map(ReplyInfoResponse::from).toList();
        }
        return CommentInfoResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
//                .childComments(list)
                .CommentCount(list.size())
                .replyFlag(comment.isReplyFlag())
                .build();
    }

}
