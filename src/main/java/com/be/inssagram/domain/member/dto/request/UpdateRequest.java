package com.be.inssagram.domain.member.dto.request;


import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UpdateRequest {
    private String email;
    private String nickname;
    private String jobField;
    private String password;
}
