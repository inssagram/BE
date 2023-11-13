package com.be.inssagram.domain.follow.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.service.FollowService;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class FollowController {

    private final FollowService followService;
    private final TokenProvider tokenProvider;

    @PostMapping("/follow")
    public ApiResponse<String> followMember(@RequestHeader("Authorization") String token,
                                       @RequestBody FollowRequest request){
        Member member = tokenProvider.getMemberFromToken(token);
        String result = followService.follow(member, request);
        return ApiResponse.createMessage(result);
    }

    @GetMapping("/follow/recommend")
    public ApiResponse<Page<InfoResponse>> recommendMembers(@RequestHeader("Authorization") String token,
                                                            @RequestParam(defaultValue = "0") int pageNumber,
                                                            @RequestParam(defaultValue = "10") int pageSize
                                           ){
        Member member = tokenProvider.getMemberFromToken(token);
        Page<InfoResponse> result = followService.recommendFollowing(member, pageNumber, pageSize);
        return ApiResponse.createSuccessWithMessage(result,"팔로우 추천 리스트를 불러왔습니다");
    }
}
