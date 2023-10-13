package com.be.inssagram.domain.like.service;

import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.comment.repository.CommentRepository;
import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.comment.CommentDoesNotExistException;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void onLikePost(Long postId, Long memberId) {
        // 유저(Member)를 찾아옵니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(UserDoesNotExistException::new);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        Optional<Like> existingLike = likeRepository
                .findByPostIdAndMemberId(postId, memberId);

        if (existingLike.isPresent()){
            likeRepository.delete(existingLike.orElse(null));
            return;
        }
        Like like = Like.builder().post(post).member(member).build();
        likeRepository.save(like);
    }

    public void onLikeComment(Long commentId, Long memberId) {

        // 댓글(Comment)를 찾아옵니다.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        // 유저(Member)를 찾아옵니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(UserDoesNotExistException::new);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(comment.getPost().getId())
                .orElseThrow(PostDoesNotExistException::new);

        Optional<Like> existingLike = likeRepository
                .findByPostIdAndMemberIdAndCommentId(
                        post.getId(), memberId, commentId);
        if (existingLike.isPresent()){
            likeRepository.delete(existingLike.orElse(null));
            return;
        }
        Like like = Like.builder().post(post).member(member).comment(comment)
                .build();
        likeRepository.save(like);
    }

    public List<InfoResponse> searchMemberLikePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);
        List<Like> likeList = likeRepository.findByPostAndCommentId(
                post, null);

        return likeList.stream().map(Like::getMember)
                .map(InfoResponse::fromEntity)
                .toList();
    }

    public List<InfoResponse> searchMemberLikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        List<Like> likeList = likeRepository.findByComment(comment);

        return likeList.stream().map(Like::getMember)
                .map(InfoResponse::fromEntity)
                .toList();
    }

}
