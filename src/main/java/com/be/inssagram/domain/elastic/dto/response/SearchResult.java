package com.be.inssagram.domain.elastic.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResult {
    private Long memberId;
    private Long hashtagId;
    private String hashtagName;
    private String memberEmail;
    private String memberNickname;
    private String memberCompanyName;
    private String searched;

    public static SearchResult createHashtagResult(Long hashtagId, String hashtagName) {
        SearchResult result = new SearchResult();
        result.setHashtagId(hashtagId);
        result.setHashtagName(hashtagName);
        return result;
    }

    public static SearchResult createMemberResult(Long memberId, String memberEmail, String memberNickname, String memberCompanyName) {
        SearchResult result = new SearchResult();
        result.setMemberId(memberId);
        result.setMemberEmail(memberEmail);
        result.setMemberNickname(memberNickname);
        result.setMemberCompanyName(memberCompanyName);
        return result;
    }

    public static SearchResult createSearchHistoryResult(String searched) {
        SearchResult result = new SearchResult();
        result.setSearched(searched);
        return result;
    }
}
