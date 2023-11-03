package com.be.inssagram.domain.elastic.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.elastic.dto.request.SearchRequest;
import com.be.inssagram.domain.elastic.dto.response.SearchResult;
import com.be.inssagram.domain.elastic.service.ElasticSearchService;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ElasticSearchController {

    private final ElasticSearchService elasticsearchService;
    private final TokenProvider tokenProvider;

    @GetMapping("/search/{keyword}")
    public List<SearchResult> searchMemberAndHashtag(
            @PathVariable String keyword,
            @RequestHeader(value = "Authorization", required = false) String token) {
        InfoResponse member;
        if(token != null) {
             member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        } else{
             member = null;
        }
        return elasticsearchService.search(keyword, member);
    }

    @GetMapping("/search")
    public List<SearchResult> getMySearchHistory(
            @RequestHeader(value = "Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        return elasticsearchService.getSearchHistoryList(member.member_id());
    }

    @DeleteMapping("/search/{keyword}")
    public ApiResponse<?> deleteHistory(
            @PathVariable String keyword,
            @RequestHeader(value = "Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        elasticsearchService.deleteSearchHistory(keyword, member.member_id());
        return ApiResponse.createMessage("검색 기록이 삭제되었습니다");
    }
}
