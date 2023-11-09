package com.be.inssagram.domain.like.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.comment.repository.CommentRepository;
import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.comment.CommentDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final NotificationService notificationService;

    private final TokenProvider tokenProvider;

    public String onLikePost(String token, Long postId) {
        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        Optional<Like> existingLike = likeRepository
                .findByPostIdAndMemberId(postId, member.getId());

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.orElse(null));
            return "빈 하트";
        }
        Like like = Like.builder().post(post).member(member).build();
        //자신이 이외의 사람이 좋아요를 눌렀을때 작성자에게 알림을 전송합니다
        if(!post.getMember().getId().equals(member.getId())) {
            notificationService.notify(notificationService
                    .createNotifyDto(
                            post.getMember(),
                            post,
                            member,
                            member.getNickname()+"님이 회원님의 게시물을 좋아합니다"
                    ));
        }
        likeRepository.save(like);
        return "하트";
    }

    public String onLikeComment(String token, Long commentId) {

        // 댓글(Comment)를 찾아옵니다.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(comment.getPost().getId())
                .orElseThrow(PostDoesNotExistException::new);

        Optional<Like> existingLike = likeRepository
                .findByPostIdAndMemberIdAndCommentId(
                        post.getId(), member.getId(), commentId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.orElse(null));
            return "빈 하트";
        }
        Like like = Like.builder().post(post).member(member).comment(comment)
                .build();
        //자신 이외의 유저가 좋아요를 눌렀을시 작성자에게 알림을 전송합니다.
        if(!comment.getMember().getId().equals(member.getId())) {
            notificationService.notify(notificationService
                    .createNotifyDto(
                            comment.getMember(),
                            post,
                            member,
                            member.getNickname()+"님이 회원님의 댓글을 좋아합니다:"+comment.getContent()
                    ));
        }
        likeRepository.save(like);
        return "하트";
    }

    @Transactional
    public List<InfoResponse> searchMemberLikePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);
        List<Like> likeList = likeRepository.findByPostAndCommentId(
                post, null);

        return likeList.stream().map(Like::getMember)
                .map(InfoResponse::fromEntity)
                .toList();
    }

    @Transactional
    public List<InfoResponse> searchMemberLikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        List<Like> likeList = likeRepository.findByComment(comment);

        return likeList.stream().map(Like::getMember)
                .map(InfoResponse::fromEntity)
                .toList();
    }

}
