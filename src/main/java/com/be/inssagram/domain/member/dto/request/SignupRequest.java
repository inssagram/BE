package com.be.inssagram.domain.member.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String authNumber;
    @NotBlank
    private String gender;
    @NotBlank
    private String nickname;
    @NotBlank
    private String jobField;
}
