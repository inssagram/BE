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
            @RequestBody(required = false) SearchRequest memberId) {

        return elasticsearchService.search(keyword, memberId);
    }

    @GetMapping("/search")
    public List<SearchResult> searchMemberAndHashtag(
            @RequestHeader(value = "Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        return elasticsearchService.getSearchHistoryList(member.member_id());
    }

    @DeleteMapping("/search/{keyword}")
    public ApiResponse<?> deleteHistory(
            @PathVariable String keyword,
            @RequestBody(required = false) SearchRequest memberId) {

        elasticsearchService.deleteSearchHistory(keyword, memberId);
        return ApiResponse.createMessage("검색 기록이 삭제되었습니다");
    }
}
