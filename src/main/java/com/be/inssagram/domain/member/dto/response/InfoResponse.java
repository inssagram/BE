package com.be.inssagram.domain.member.dto.response;

import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record InfoResponse (
        String email,
        String nickname,
        String gender,
        String jobField
) {

    public static InfoResponse fromEntity(Member member) {
        return InfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .jobField(member.getCompanyName())
                .build();
    }
}