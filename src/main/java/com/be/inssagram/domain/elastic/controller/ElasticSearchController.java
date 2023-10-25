package com.be.inssagram.domain.elastic.controller;


import com.be.inssagram.domain.elastic.dto.response.SearchMemberResult;
import com.be.inssagram.domain.elastic.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ElasticSearchController {

    private final ElasticSearchService elasticsearchService;

    @GetMapping("/search/member/{wildcardValue}")
    public List<SearchMemberResult> searchMember(
            @PathVariable String wildcardValue) {

        return elasticsearchService.executeWildcardQuery(wildcardValue);
    }
}
