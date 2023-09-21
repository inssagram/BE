package com.be.inssagram.domain.member.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateRequest {
    private String email;
    private String nickname;
    private String gender;
    private String jobField;
    private String password;
}
