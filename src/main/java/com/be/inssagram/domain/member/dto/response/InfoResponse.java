package com.be.inssagram.domain.member.dto.response;

import com.be.inssagram.domain.follow.dto.response.FollowerList;
import com.be.inssagram.domain.follow.dto.response.FollowingList;
import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record InfoResponse(
        Long id,
        String email,
        String nickname,
        String companyName,
        String profilePic
) {
    public static InfoResponse fromEntity(Member member) {
        return InfoResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .companyName(member.getCompanyName())
                .profilePic(member.getProfilePic())
                .build();
    }
}