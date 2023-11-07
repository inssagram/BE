package com.be.inssagram.domain.follow.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.service.FollowService;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final TokenProvider tokenProvider;

    @PostMapping("/member/follow")
    public ApiResponse<?> followMember(@RequestHeader("Authorization") String token,
                                       @RequestBody FollowRequest request){
        Member member = tokenProvider.getMemberFromToken(token);
        String result = followService.follow(member, request);
        return ApiResponse.createMessage(result);
    }
}
