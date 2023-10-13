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

    @PostMapping("/member/follow")
    public ApiResponse<?> followMember(@RequestBody FollowRequest request){
        String result = followService.follow(request);
        return ApiResponse.createMessage(result);
    }
}
