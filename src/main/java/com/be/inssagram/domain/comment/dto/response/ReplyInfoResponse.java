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
public class ReplyInfoResponse {

    private Long commentId;
    private Long postId;
    private Long memberId;
    private String nickName;
    private String content;
    private boolean replyFlag;
    private Integer likeCount;
    private String createdAt;
    private List<String> mentionList;

    public static ReplyInfoResponse from(Comment comment) {

        return ReplyInfoResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .nickName(comment.getMember().getNickname())
                .content(comment.getContent())
                .replyFlag(comment.isReplyFlag())
                .createdAt(comment.getCreatedAt())
                .mentionList(comment.getMentionList())
                .build();
    }

}
