package com.be.inssagram.domain.member.dto.response;

import com.be.inssagram.domain.member.documents.SearchMember;
import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record InfoResponse(
        String email,
        String nickname,
        String companyName,
        String profilePic,
        String description


) {

    public static InfoResponse fromEntity(Member member) {
        return InfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .companyName(member.getCompanyName())
                .profilePic(member.getProfilePic())
                .description(member.getDescription())
                .build();
    }
}