package com.be.inssagram.domain.hashTag.repository;

import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    List<HashTag> findByName(String name);

    List<HashTag> findByPost(Post post);

    HashTag findByPostAndName(Post post, String name);

}
