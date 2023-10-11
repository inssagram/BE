package com.be.inssagram.domain.elastic.service;


import com.be.inssagram.domain.elastic.dto.response.SearchMemberResult;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final RestTemplate restTemplate;

    @Value("${spring.elastic.host}")
    private String elasticsearchHost;

    @Value("${spring.elastic.port}")
    private int elasticsearchPort;

    public List<SearchMemberResult> executeWildcardQuery(String wildcardValue) {
        String endpoint = "/members/_search";
        String requestBody = "{ \"query\": { \"wildcard\": { \"member_nickname\": { \"value\": \"*" + wildcardValue + "*\" } } } }";
        String elasticsearchUrl = "http://" + elasticsearchHost + ":" + elasticsearchPort + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(elasticsearchUrl, requestEntity, String.class);
        String responseJson = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root;
        try {
            root = objectMapper.readTree(responseJson);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        List<SearchMemberResult> results = new ArrayList<>();
        JsonNode hitsArray = root.path("hits").path("hits");
        for (JsonNode hit : hitsArray) {
            JsonNode source = hit.path("_source");
            SearchMemberResult result = new SearchMemberResult();
            result.setMemberId(source.path("member_id").asLong());
            result.setMemberEmail(source.path("member_email").asText());
            result.setMemberNickname(source.path("member_nickname").asText());
            result.setMemberCompanyName(source.path("member_company_name").asText());
            results.add(result);
        }

        return results;
    }
}
