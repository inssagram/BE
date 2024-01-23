package com.be.inssagram.domain.bookmark.repository;

import com.be.inssagram.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByMemberIdAndPostId (Long memberId, Long PostId);

    List<Bookmark> findAllByMemberId (Long memberId);

    List<Bookmark> findAllByMemberIdAndPostId(Long memberId, Long postId);
}
