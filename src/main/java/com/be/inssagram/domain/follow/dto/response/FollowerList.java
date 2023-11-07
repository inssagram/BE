package com.be.inssagram.domain.follow.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerList {
    private Long followerId;
    private String followerName;
    private String followerImage;

    public FollowerList(Long myId, String myNickname, String followerImage) {
        this.followerId = myId;
        this.followerName = myNickname;
        this.followerImage = followerImage;
    }
}
