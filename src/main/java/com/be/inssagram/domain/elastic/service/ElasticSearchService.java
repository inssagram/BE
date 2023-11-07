package com.be.inssagram.domain.elastic.service;


import com.be.inssagram.domain.elastic.documents.index.HashtagIndex;
import com.be.inssagram.domain.elastic.documents.index.HistoryIndex;
import com.be.inssagram.domain.elastic.documents.repository.HashtagSearchRepository;
import com.be.inssagram.domain.elastic.documents.repository.HistorySearchRepository;
import com.be.inssagram.domain.elastic.dto.request.SearchRequest;
import com.be.inssagram.domain.elastic.dto.response.SearchResult;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.common.DataDoesNotExistException;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final RestTemplate restTemplate;
    private final HistorySearchRepository historySearchRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final HashtagSearchRepository hashtagSearchRepository;

    @Value("${spring.elastic.url}")
    private String elasticUrl;


    //검색 기능
    public List<SearchResult> search (String value, Member myInfo) {
        String endpoint = "/members,hashtags/_search";
        String requestBody = "{ \"query\": { \"wildcard\": { \"name\": { \"value\": \"*" + value + "*\" } } } }";
        String elasticsearchUrl = "http://" + elasticUrl + endpoint;

        //검색 정보를 JsonNode 로 받아오기, 없으면 빈 배열 반환
        JsonNode root;
        try {
            root = getResponses(requestBody, elasticsearchUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        //검색 정보가 있을시 구분하여 JSON 값 작성및 반환
        List<SearchResult> results = new ArrayList<>();
        JsonNode hitsArray = root.path("hits").path("hits");
        for (JsonNode hit : hitsArray) {
            JsonNode source = hit.path("_source");
            if (hit.path("_index").asText().equals("members")) {
                Member memberInfo = memberRepository.findById(source.path("id").asLong())
                        .orElseThrow(UserDoesNotExistException::new);
                //검색기록중 친구상태값 반환
                Follow isFriend = followRepository.findByRequesterInfoAndFollowingInfo(myInfo , memberInfo);
                Boolean status = false;
                if(isFriend != null){
                    status = true;
                }
                SearchResult memberResult = SearchResult.createMemberResult(
                        source.path("id").asLong(),
                        source.path("email").asText(),
                        source.path("name").asText(),
                        source.path("job").asText(),
                        source.path("image").asText(),
                        status
                );
                results.add(memberResult);
            } else {
                SearchResult hashtagResult = SearchResult.createHashtagResult(
                        "#" + source.path("name").asText()
                );
                results.add(hashtagResult);
            }
        }
        return results;
    }

    //최근 검색기록 조회
    public List<SearchResult> getSearchHistoryList(Long memberId) {
        //아무것도 적지 않았을때 마지막으로 검색했던 기록을 보여줌
        String endpoint = "/histories/_search";
        String requestBody = "{\"query\": {\"term\": {\"member_id\":" + memberId + "}}, " +
                "\"sort\": [{\"_id\": {\"order\": \"desc\"}}], " +
                "\"size\": 5}";
        String elasticsearchUrl = "http://" + elasticUrl + endpoint;

        JsonNode root;
        try {
            root = getResponses(requestBody, elasticsearchUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        List<SearchResult> results = new ArrayList<>();
        JsonNode hitsArray = root.path("hits").path("hits");
        for (JsonNode hit : hitsArray) {
            JsonNode source = hit.path("_source");
            SearchResult historyResult = SearchResult.createSearchHistoryResult(
                    source.path("search_id").asLong(),
                    source.path("searched").asText(),
                    source.path("image").asText()
            );
            results.add(historyResult);
        }
        return results;
    }

    //검색 기록 저장
    public String saveSearch(Long memberId, SearchRequest request){
        if(request.getHashtagName() == null){
            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(UserDoesNotExistException::new);
            HistoryIndex newHistory = HistoryIndex.builder()
                    .createdAt(LocalDateTime.now())
                    .memberId(memberId)
                    .searched(member.getNickname())
                    .search_id(member.getId())
                    .image(member.getImage())
                    .build();
            historySearchRepository.save(newHistory);
            return "성공적으로 회원 검색을 저장하였습니다";
        }
        HashtagIndex hashtag = hashtagSearchRepository.findByName(request.getHashtagName());
        HistoryIndex newHistory = HistoryIndex.builder()
                .createdAt(LocalDateTime.now())
                .memberId(memberId)
                .searched("#"+hashtag.getName())
                .build();
        historySearchRepository.save(newHistory);
        return "성공적으로 해시태그 검색을 저장하였습니다";
    }

    //최근 검색기록 삭제
    public void deleteSearchHistory(String value, Long memberId){
        String endpoint = "/histories/_doc/_delete_by_query";
        String requestBody = "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"searched\":\"" + value + "\"}}," +
                " {\"term\":{\"member_id\":" + memberId + "}}]}}}";
        String elasticsearchUrl = "http://" + elasticUrl + endpoint;

        sendHttpRequest(requestBody, elasticsearchUrl);
    }

    private JsonNode getResponses(String requestBody, String elasticsearchUrl) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = sendHttpRequest(requestBody, elasticsearchUrl);
        String responseJson = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseJson);
    }

    private ResponseEntity<String> sendHttpRequest (String requestBody, String elasticsearchUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(elasticsearchUrl, requestEntity, String.class);
    }
}
