package com.be.inssagram.domain.member.dto.response;

import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record InfoResponse(
        Long member_id,
        String email,
        String nickname,
        String job,
        String image
) {
    public static InfoResponse fromEntity(Member member) {
        return InfoResponse.builder()
                .member_id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .job(member.getJob())
                .image(member.getImage())
                .build();
    }
}