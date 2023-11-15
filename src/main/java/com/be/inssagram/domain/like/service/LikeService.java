package com.be.inssagram.domain.like.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.comment.repository.CommentRepository;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.like.type.LikeType;
import com.be.inssagram.domain.member.dto.response.LikeMemberInfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.stroy.entity.Story;
import com.be.inssagram.domain.stroy.repository.StoryRepository;
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
    private final FollowRepository followRepository;
    private final StoryRepository storyRepository;

    private final NotificationService notificationService;

    private final TokenProvider tokenProvider;

    public String onLikePost(String token, Long postId) {
        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        Optional<Like> existingLike = likeRepository
                .findByMemberIdAndLikeTypeAndAndLikeTypeId(
                        member.getId(), LikeType.post, postId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.orElse(null));
            return "빈 하트";
        }
        Like like = Like.builder()
                .member(member)
                .likeType(LikeType.post)
                .likeTypeId(postId)
                .build();
        //자신이 이외의 사람이 좋아요를 눌렀을때 작성자에게 알림을 전송합니다
        if (!post.getMember().getId().equals(member.getId())) {
            notificationService.notify(notificationService
                    .createNotifyDto(
                            post.getMember(),
                            post,
                            member,
                            member.getNickname() + "님이 회원님의 게시물을 좋아합니다",
                            null
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
                .findByMemberIdAndLikeTypeAndAndLikeTypeId(
                        member.getId(), LikeType.comment, commentId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.orElse(null));
            return "빈 하트";
        }
        Like like = Like.builder()
                .member(member)
                .likeType(LikeType.comment)
                .likeTypeId(commentId)
                .build();
        //자신 이외의 유저가 좋아요를 눌렀을시 작성자에게 알림을 전송합니다.
        if (!comment.getMember().getId().equals(member.getId())) {
            notificationService.notify(notificationService
                    .createNotifyDto(
                            comment.getMember(),
                            post,
                            member,
                            member.getNickname() + "님이 회원님의 댓글을 좋아합니다: " + comment.getContent(),
                            null
                    ));
        }
        likeRepository.save(like);
        return "하트";
    }

    public String onLikeStory(String token, Long storyId) {

        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        Story story = storyRepository.findById(storyId).orElseThrow();

        Optional<Like> existingLike = likeRepository
                .findByMemberIdAndLikeTypeAndAndLikeTypeId(
                        member.getId(), LikeType.story, storyId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.orElse(null));
            return "빈 하트";
        }

        Like like = Like.builder()
                .member(member)
                .likeType(LikeType.story)
                .likeTypeId(storyId)
                .build();
        likeRepository.save(like);
        return "하트";
    }

    @Transactional
    public List<LikeMemberInfoResponse> searchMemberLikePost(
            String token, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        List<Like> likeList = likeRepository.findByLikeTypeAndLikeTypeId(
                LikeType.post, postId);

        return getLikeMemberInfoResponses(token, likeList);
    }

    @Transactional
    public List<LikeMemberInfoResponse> searchMemberLikeComment(
            String token, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        List<Like> likeList = likeRepository.findByLikeTypeAndLikeTypeId(
                LikeType.comment, commentId);

        return getLikeMemberInfoResponses(token, likeList);
    }

    private List<LikeMemberInfoResponse> getLikeMemberInfoResponses(String token, List<Like> likeList) {
        Long memberId = tokenProvider.getMemberFromToken(token).getId();

        List<LikeMemberInfoResponse> responses = likeList.stream()
                .map(Like::getMember)
                .map(LikeMemberInfoResponse::from)
                .toList();

        for (LikeMemberInfoResponse response : responses) {
            if (followRepository.findByRequesterInfoIdAndFollowingInfoId(
                    memberId, response.getMemberId()).isPresent()) {
                response.setFollowedState(true);
            }
        }

        return responses;
    }

}
