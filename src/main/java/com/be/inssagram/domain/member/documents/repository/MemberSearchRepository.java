package com.be.inssagram.domain.member.documents.repository;

import com.be.inssagram.domain.member.documents.index.SearchMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@EnableElasticsearchRepositories
public interface MemberSearchRepository extends ElasticsearchRepository<SearchMember,Long> {

}
