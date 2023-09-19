package com.be.inssagram.domain.member.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String gender;
    private String nickname;
    private String jobField;
}
