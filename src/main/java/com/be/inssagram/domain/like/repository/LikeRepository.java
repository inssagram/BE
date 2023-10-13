package com.be.inssagram.domain.like.repository;

import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPostAndCommentId(Post post, Long commentId);

    List<Like> findByComment(Comment comment);

    Optional<Like> findByPostIdAndMemberId(Long postId, Long memberId);

    Optional<Like> findByPostIdAndMemberIdAndCommentId(
            Long postId, Long memberId, Long commentId);
}
