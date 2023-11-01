package com.be.inssagram.domain.tag.dto.request;

import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record TagCreateRequest(
        Long memberId,
        Long postId
) {

}
