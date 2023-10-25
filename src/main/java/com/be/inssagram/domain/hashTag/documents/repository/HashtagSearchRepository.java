package com.be.inssagram.domain.hashTag.documents.repository;

import com.be.inssagram.domain.hashTag.documents.index.SearchHashtag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface HashtagSearchRepository extends ElasticsearchRepository<SearchHashtag, Long> {
}
