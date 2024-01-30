package com.be.inssagram.domain.follow.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowingList {
    private Long following_Id;
    private String following_Name;
    private String following_Image;
    private String following_Description;

    public FollowingList(Long memberId, String memberName, String memberImage, String memberDescription) {
        this.following_Id = memberId;
        this.following_Name = memberName;
        this.following_Image = memberImage;
        this.following_Description = memberDescription;
    }

}
