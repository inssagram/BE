package com.be.inssagram.domain.member.dto.request;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SearchRequest {
    private String email;
    private String nickname;
    private String companyName;
}
