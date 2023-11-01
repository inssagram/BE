package com.be.inssagram.domain.follow.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowingList {
    private Long memberId;
    private String memberName;

    public FollowingList(Long memberId, String memberNickname) {
        this.memberId = memberId;
        this.memberName = memberNickname;
    }
}
