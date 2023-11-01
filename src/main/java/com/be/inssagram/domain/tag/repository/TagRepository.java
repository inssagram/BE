package com.be.inssagram.domain.tag.repository;

import com.be.inssagram.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByPostIdAndImageId(Long postId, Long imageId);
    Tag findByPostIdAndMemberIdAndImageId(Long postId, Long memberId, Long imageId);

    List<Tag> findByMemberIdAndAndImageId(Long memberId, Long imageId);
}
