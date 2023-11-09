package com.be.inssagram.domain.elastic.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResult {
    private Long memberId;
    private String email;
    private String nickName;
    private String job;
    private String searched;
    private Boolean friendStatus;
    private String image;

    public static SearchResult createHashtagResult(String hashtagName) {
        SearchResult result = new SearchResult();
        result.setNickName(hashtagName);
        return result;
    }

    public static SearchResult createMemberResult(Long memberId, String memberEmail,
                                                  String memberNickname, String memberCompanyName, String image, Boolean friendStatus) {
        SearchResult result = new SearchResult();
        result.setMemberId(memberId);
        result.setEmail(memberEmail);
        result.setNickName(memberNickname);
        result.setJob(memberCompanyName);
        result.setFriendStatus(friendStatus);
        result.setImage(image);
        return result;
    }

    public static SearchResult createSearchHistoryResult(Long id, String searched, String image, boolean friendStatus, String job) {
        if(searched.contains("#")){
            SearchResult result = new SearchResult();
            result.setSearched(searched);
            result.setImage(image);
            result.setFriendStatus(friendStatus);
            return result;
        }
        SearchResult result = new SearchResult();
        result.setMemberId(id);
        result.setSearched(searched);
        result.setImage(image);
        result.setJob(job);
        result.setFriendStatus(friendStatus);
        return result;
    }
}
