package com.be.inssagram.domain.follow.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerList {
    private Long followerId;
    private String followerName;

    public FollowerList(Long memberId, String memberName) {
        this.followerId = memberId;
        this.followerName = memberName;
    }
}
