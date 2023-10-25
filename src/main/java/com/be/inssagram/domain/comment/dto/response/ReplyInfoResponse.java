package com.be.inssagram.domain.comment.dto.response;

import com.be.inssagram.domain.comment.entity.Comment;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyInfoResponse {

    private Long commentId;
    private Long postId;
    private Long memberId;
    private String content;
    private boolean replyFlag;
    private Long targetMemberId;
    private Integer likeCount;

    public static ReplyInfoResponse from(Comment comment) {

        return ReplyInfoResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .content(comment.getContent())
                .replyFlag(comment.isReplyFlag())
                .build();
    }

}