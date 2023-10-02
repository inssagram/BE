package com.be.inssagram.domain.member.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.member.dto.request.AuthenticationRequest;
import com.be.inssagram.domain.member.dto.request.SigninRequest;
import com.be.inssagram.domain.member.dto.request.SignupRequest;
import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Auth;
import com.be.inssagram.domain.member.repository.AuthRepository;
import com.be.inssagram.domain.member.service.MailService;
import com.be.inssagram.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;
    private final AuthRepository authRepository;

    //사용할수 있는 이메일인지 확인
    @GetMapping("/signup/check")
    public ApiResponse<?> checkEmail(@RequestBody AuthenticationRequest request){
        memberService.checkAvailability(request);
        return ApiResponse.createMessage("사용하실 수 있는 이메일 입니다");
    }

    //이메일 인증번호 발급
    @PostMapping("/signup/auth")
    public ApiResponse<?> mailSend(@RequestBody AuthenticationRequest request){
        int authCode = mailService.sendMail(request);
        String code = authCode + "";
        Auth temp = Auth.builder()
                .email(request.getEmail())
                .code(code)
                .build();
        authRepository.save(temp);
        return ApiResponse.createMessage("인증번호가 발급되었습니다");
    }

    //인증번호 확인
    @PostMapping("/signup/auth/check")
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
    public ApiResponse<?> signin(@Valid @RequestBody SigninRequest request){
        memberService.signin(request);
        return ApiResponse.createMessage("로그인 되셧습니다");
    }

    //회원수정
    @PutMapping("/member/update/{id}")
    public ApiResponse<InfoResponse> update(@PathVariable Long id,
                                            @RequestBody UpdateRequest request){
        InfoResponse result = memberService.updateMember(id, request);
        return ApiResponse.createSuccessWithMessage(result, "정상적으로 수정되었습니다");
    }

    //회원탈퇴
    @DeleteMapping("/member/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id){
        memberService.deleteMember(id);
        return ApiResponse.createMessage("정상적으로 삭제되었습니다");
    }
}
