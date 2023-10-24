package com.be.inssagram.domain.like.dto.response;

import com.be.inssagram.domain.like.entity.Like;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeInfoResponse {
    private Long commentId;
    private Long postId;
    private Long memberId;

    public static LikeInfoResponse from(Like like) {
        Long tempCommentId = -1L;
        if (like.getComment() != null) {
            tempCommentId = like.getComment().getId();
        }
        return LikeInfoResponse.builder()
                .commentId(tempCommentId)
                .postId(like.getPost().getId())
                .memberId(like.getMember().getId())
                .build();
    }
}
