package com.be.inssagram.domain.member.dto.response;

import com.be.inssagram.domain.follow.dto.response.FollowerList;
import com.be.inssagram.domain.follow.dto.response.FollowingList;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record InfoResponse(
        String email,
        String nickname,
        String companyName,
        String profilePic,
        String description,
        List<FollowerList> followers,
        List<FollowingList> following,
        int posts
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

    public static InfoResponse fromEntity(Member member, List<FollowingList> following, List<FollowerList> followers) {
        return InfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .companyName(member.getCompanyName())
                .profilePic(member.getProfilePic())
                .description(member.getDescription())
                .followers(followers)
                .following(following)
                .build();
    }
}