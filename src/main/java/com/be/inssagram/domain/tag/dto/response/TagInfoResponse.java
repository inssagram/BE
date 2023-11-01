package com.be.inssagram.domain.tag.dto.response;

import com.be.inssagram.domain.tag.entity.Tag;
import lombok.Builder;

@Builder
public record TagInfoResponse(
        Long tagId,
        Long memberId,
        Long postId,
        String nickname
) {
    public static TagInfoResponse from(Tag tag) {
        return TagInfoResponse.builder()
                .tagId(tag.getId())
                .memberId(tag.getMember().getId())
                .postId(tag.getPost().getId())
                .nickname(tag.getMember().getNickname())
                .build();
    }

}
