package com.be.inssagram.domain.post.repository;

import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.type.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMemberIdAndType(Long memberId, PostType type);
}