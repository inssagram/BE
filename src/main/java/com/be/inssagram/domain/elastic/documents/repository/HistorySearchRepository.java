package com.be.inssagram.domain.elastic.documents.repository;

import com.be.inssagram.domain.elastic.documents.index.HistoryIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface HistorySearchRepository extends ElasticsearchRepository<HistoryIndex, Long> {
    HistoryIndex findByMemberIdAndSearched (Long memberId, String searched);
}
