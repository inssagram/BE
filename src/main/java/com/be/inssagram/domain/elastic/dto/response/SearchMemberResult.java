package com.be.inssagram.domain.elastic.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMemberResult {
    private Long memberId;
    private String memberEmail;
    private String memberNickname;
    private String memberCompanyName;
}
