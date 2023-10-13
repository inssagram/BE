package com.be.inssagram.domain.comment.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {
    private Long memberId;
    private String contents;

}
