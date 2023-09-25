package com.be.inssagram.domain.member.dto.request;


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
