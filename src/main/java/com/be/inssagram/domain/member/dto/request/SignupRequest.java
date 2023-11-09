package com.be.inssagram.domain.member.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SignupRequest {

    @NotNull(message = "이메일 정보가 없습니다")
    private String email;
    @NotNull(message = "비밀번호 정보가 없습니다")
    private String password;
    @NotNull(message = "닉네임 정보가 없습니다")
    private String nickname;
    @NotNull(message = "직업 정보가 없습니다")
    private String job;

    private String image;
}
