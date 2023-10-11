package com.be.inssagram.domain.follow.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/member")
    public ApiResponse<?> followMember(@RequestBody FollowRequest request){
        String result = followService.followMember(request);
        return ApiResponse.createMessage(result);
    }
    @PostMapping("/follow/hashtag")
    public ApiResponse<?> followHashtag(@RequestBody FollowRequest request){
        String result = followService.followHashtag(request);
        return ApiResponse.createMessage(result);
    }
}
