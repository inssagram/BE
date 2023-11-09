package com.be.inssagram.domain.comment.dto.response;

import com.be.inssagram.domain.comment.entity.Comment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInfoResponse {

    private Long parentId;
    private Long commentId;
    private Long postId;
    private Long memberId;
    private String nickname;
    private String memberImage;
    private String content;
    private Integer CommentCount;
    private Integer likeCount;
    private boolean replyFlag;
    private String createdAt;
    private List<String> mentionList;
    //state
    private Boolean commentLike;

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
                .nickname(comment.getMember().getNickname())
                .memberImage(comment.getMember().getImage())
                .content(comment.getContent())
                .CommentCount(list.size())
                .replyFlag(comment.isReplyFlag())
                .createdAt(comment.getCreatedAt())
                .mentionList(comment.getMentionList())
                .build();
    }

}
