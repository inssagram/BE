package com.be.inssagram.domain.hashTag.repository;

import com.be.inssagram.domain.hashTag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {

}
