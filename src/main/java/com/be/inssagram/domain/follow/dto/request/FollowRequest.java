package com.be.inssagram.domain.follow.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequest {
    private Long followId;
    private Long hashtagId;
    private Long myId;
    private String myName;
}
