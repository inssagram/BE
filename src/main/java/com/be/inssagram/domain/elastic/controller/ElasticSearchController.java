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

    //엘라스틱 검색
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

    //최근 검색기록 저장
    @PostMapping("/search/save")
    public ApiResponse<?> saveSearchData(@RequestHeader(value = "Authorization") String token,
                                         @RequestBody SearchRequest request) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        String result = elasticsearchService.saveSearch(member.member_id(), request);
        return ApiResponse.createMessage(result);
    }

    //최근 검색기록 조회
    @GetMapping("/search")
    public List<SearchResult> getMySearchHistory(
            @RequestHeader(value = "Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        return elasticsearchService.getSearchHistoryList(member.member_id());
    }

    //최근 검색기록 삭제
    @DeleteMapping("/search/{keyword}")
    public ApiResponse<?> deleteHistory(
            @PathVariable String keyword,
            @RequestHeader(value = "Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        elasticsearchService.deleteSearchHistory(keyword, member.member_id());
        return ApiResponse.createMessage("검색 기록이 삭제되었습니다");
    }
}
