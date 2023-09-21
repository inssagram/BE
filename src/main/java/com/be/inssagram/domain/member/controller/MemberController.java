package com.be.inssagram.domain.member.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.member.dto.request.SigninRequest;
import com.be.inssagram.domain.member.dto.request.SignupRequest;
import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignupRequest request){
        memberService.signup(request);
        return ApiResponse.createMessage("가입이 완료되었습니다!");
    }

    @PostMapping("/signin")
    public ApiResponse<?> signin(@Valid @RequestBody SigninRequest request){
        memberService.signin(request);
        return ApiResponse.createMessage("로그인 되셧습니다");
    }

    @PutMapping("/member/update/{id}")
    public ApiResponse<InfoResponse> update(@PathVariable Long id,
                                            @RequestBody UpdateRequest request){
        InfoResponse result = memberService.updateMember(id, request);
        return ApiResponse.createSuccessWithMessage(result, "정상적으로 수정되었습니다");
    }

    @DeleteMapping("/member/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id){
        memberService.deleteMember(id);
        return ApiResponse.createMessage("정상적으로 삭제되었습니다");
    }
}
