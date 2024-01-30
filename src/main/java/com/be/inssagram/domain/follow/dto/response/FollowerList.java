package com.be.inssagram.domain.follow.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerList {
    private Long followerId;
    private String followerName;
    private String followerImage;
    private String followerDescription;

    public FollowerList(Long myId, String myNickname, String followerImage, String followerDescription) {
        this.followerId = myId;
        this.followerName = myNickname;
        this.followerImage = followerImage;
        this.followerDescription = followerDescription;
    }
}
