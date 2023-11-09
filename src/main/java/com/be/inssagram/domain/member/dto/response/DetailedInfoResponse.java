package com.be.inssagram.domain.member.dto.response;

import com.be.inssagram.domain.follow.dto.response.FollowerList;
import com.be.inssagram.domain.follow.dto.response.FollowingList;
import com.be.inssagram.domain.member.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record DetailedInfoResponse(
        String email,
        String nickname,
        String companyName,
        String profilePic,
        String description,
        List<FollowerList> followers,
        List<FollowingList> following,
        int posts
) {
    public static DetailedInfoResponse fromEntity(Member member, List<FollowingList> following, List<FollowerList> followers) {
        return DetailedInfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .companyName(member.getJob())
                .profilePic(member.getImage())
                .description(member.getDescription())
                .followers(followers)
                .following(following)
                .build();
    }
}
