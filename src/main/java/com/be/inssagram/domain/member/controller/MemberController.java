package com.be.inssagram.domain.member.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.member.dto.request.AuthenticationRequest;
import com.be.inssagram.domain.member.dto.request.SigninRequest;
import com.be.inssagram.domain.member.dto.request.SignupRequest;
import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import com.be.inssagram.domain.member.dto.response.DetailedInfoResponse;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    //사용할수 있는 이메일인지 확인
    @PostMapping("/signup/check/email")
    public ApiResponse<?> checkEmail(@RequestBody AuthenticationRequest request){
        memberService.checkEmailAvailability(request);
        return ApiResponse.createMessage("사용하실 수 있는 이메일 입니다");
    }

    //사용할수 있는 닉네임인지 확인
    @PostMapping("/signup/check/nickname")
    public ApiResponse<?> checkNickname(@RequestBody AuthenticationRequest request){
        memberService.checkNicknameAvailability(request);
        return ApiResponse.createMessage("사용하실 수 있는 닉네임 입니다");
    }

    //이메일 인증번호 발급
    @PostMapping("/signup/auth")
    public ApiResponse<?> mailSend(@RequestBody AuthenticationRequest request){
        memberService.sendCode(request);
        return ApiResponse.createMessage("인증번호가 발급되었습니다");
    }

    //인증번호 확인
    @PostMapping("/signup/check/code")
    public ApiResponse<?> authCodeCheck(@RequestBody AuthenticationRequest request){
        memberService.authCodeCheck(request);
        return ApiResponse.createMessage("인증번호가 일치합니다");
    }

    //회원가입
    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignupRequest request){
        memberService.signup(request);
        return ApiResponse.createMessage("가입이 완료되었습니다!");
    }

    //로그인
    @PostMapping("/signin")
    public ApiResponse<InfoResponse> signin(@Valid @RequestBody SigninRequest request, HttpServletResponse response){
        Member member = memberService.signin(request);
        String token = tokenProvider.generateToken(member.getEmail());
        response.addHeader("Authorization", "Bearer" + " " + token);
        return ApiResponse.createSuccessWithMessage(InfoResponse.fromEntity(member),"로그인 되셧습니다");
    }

    //회원수정
    @PutMapping("/member/update/{id}")
    public ApiResponse<InfoResponse> update(@PathVariable Long id,
                                            @RequestBody UpdateRequest request,
                                            @RequestHeader("Authorization") String token){
        InfoResponse result = memberService.updateMember(id, request, token);
        return ApiResponse.createSuccessWithMessage(result, "정상적으로 수정되었습니다");
    }

    //회원탈퇴
    @DeleteMapping("/member/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id,
                                 @RequestHeader("Authorization") String token){
        memberService.deleteMember(id, token);
        return ApiResponse.createMessage("정상적으로 삭제되었습니다");
    }

    //회원 상세조회
    @GetMapping("/member/detail/{id}")
    public ApiResponse<DetailedInfoResponse> getMemberDetail(@PathVariable Long id){
        DetailedInfoResponse result = memberService.getMemberDetail(id);
        return ApiResponse.createSuccessWithMessage(result, "정보를 불러왔습니다");
    }
}
