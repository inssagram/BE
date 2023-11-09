package com.be.inssagram.domain.elastic.documents.repository;

import com.be.inssagram.domain.elastic.documents.index.HashtagIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface HashtagSearchRepository extends ElasticsearchRepository<HashtagIndex, Long> {
    HashtagIndex findByName(String name);
}
