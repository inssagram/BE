package com.be.inssagram.domain.member.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.member.dto.SignupRequest;
import com.be.inssagram.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignupRequest request){
        memberService.signup(request);
        return ApiResponse.createSuccess("가입이 완료되었습니다!");
    }
}
