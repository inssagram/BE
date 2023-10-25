package com.be.inssagram.domain.elastic.documents.repository;

import com.be.inssagram.domain.elastic.documents.index.History;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface HistorySearchRepository extends ElasticsearchRepository<History, Long> {
}
