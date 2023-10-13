package com.be.inssagram.domain.comment.repository;

import com.be.inssagram.domain.comment.dto.response.CommentInfoResponse;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 한 포스트에서 부모 댓글 들만 조회
        // 오래 발생
    @Query("SELECT c FROM COMMENT c WHERE c.post = :post AND c.replyFlag = false order by c.id asc")
    List<Comment> findParentCommentsByPost(@Param("post") Post post);
        // 오류 해결 및 쿼리 개수 감소.
    @Query("SELECT NEW com.be.inssagram.domain.comment.dto.response.CommentInfoResponse(" +
            "c.id, c.post.id, c.member.id, c.content, c.replyFlag) " +
            "FROM COMMENT c WHERE c.post = :post AND c.replyFlag = false ORDER BY c.id ASC")
    List<CommentInfoResponse> findParentCommentsByPostAndIsReply(@Param("post") Post post);
    List<Comment> findByPostAndReplyFlagIsFalse(Post post);

    // 한 부모 댓글의 대댓글 조회
    @Query("SELECT NEW com.be.inssagram.domain.comment.dto.response.CommentInfoResponse(" +
            "c.id, c.post.id, c.member.id, c.content, c.replyFlag) " +
            "FROM COMMENT c WHERE c.parentComment = :parentComment")
    List<CommentInfoResponse> findRepliesByParentComment(@Param("parentComment") Comment parentComment);
    List<Comment> findByParentComment(Comment parentComment);
}
