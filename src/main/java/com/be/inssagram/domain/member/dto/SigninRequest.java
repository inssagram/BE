package com.be.inssagram.domain.member.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SigninRequest {
    private String email;
    private String password;
}
