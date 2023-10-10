package com.be.inssagram.domain.member.documents;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@EnableElasticsearchRepositories
public interface MemberSearchRepository extends ElasticsearchRepository<SearchMember,Long> {

    List<SearchMember> findByEmail(String email);

    List<SearchMember> findByNickname(String nickname, Pageable pageable);
}
