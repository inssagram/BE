package com.be.inssagram.domain.comment.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.comment.dto.request.CommentRequest;
import com.be.inssagram.domain.comment.dto.response.CommentInfoResponse;
import com.be.inssagram.domain.comment.dto.response.ReplyInfoResponse;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.comment.repository.CommentRepository;
import com.be.inssagram.domain.like.dto.response.LikeInfoResponse;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.comment.CannotCreateCommentException;
import com.be.inssagram.exception.comment.CommentDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public CommentInfoResponse createComment(
            String token, CommentRequest request) {
        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostDoesNotExistException::new);

        //멘션 리스트가 비어있을 때를 처리합니다.
        List<String> mentionList = Optional.ofNullable(request.getMentionList())
                .orElse(new ArrayList<>());
      
        // 댓글(Comment) 객체를 생성합니다.
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(request.getContents())
                .mentionList(mentionList)
                .build();

        CommentInfoResponse response = CommentInfoResponse.from(
                commentRepository.save(comment));

        if (!mentionList.isEmpty()) {
            notifyMentionedMembers(mentionList, member, post, request.getContents());
        }

        // 게시물 작성자에게 알림을 전송합니다
        if (!post.getMember().getId().equals(member.getId())) {
            notifyPostAuthor(post, member, request.getContents());
        }
        // 댓글을 저장합니다.
        return response;
    }

    @Transactional
    public ReplyInfoResponse createReply(
            String token, CommentRequest request) {
        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 부모 댓글을 찾아옵니다.
        Comment parentComment = commentRepository.findById(request.getParentCommentId())
                .orElseThrow(CommentDoesNotExistException::new);

        // isReply 값 확인
        if (parentComment.isReplyFlag()) {
            throw new CannotCreateCommentException();
        }

        // 대댓글을 생성합니다.
        Comment savedReply = getSavedReply(request, member, parentComment);
        ReplyInfoResponse response = ReplyInfoResponse.from(savedReply);

        // 댓글 작성자에게 알림을 전송합니다
        if (!parentComment.getMember().getId().equals(member.getId())) {
            notifyOriginalCommentAuthor(parentComment, member, savedReply.getContent());
        }

        // 대댓글을 저장합니다.
        return response;
    }

    @Transactional
    public ReplyInfoResponse createReplyToReply(
            String token, CommentRequest request
    ) {
        Member member = tokenProvider.getMemberFromToken(token);

        Comment parentComment = commentRepository.findById(request.getParentCommentId())
                .orElseThrow(CommentDoesNotExistException::new);
        Comment replyComment = commentRepository.findById(request.getReplyId())
                .orElseThrow(CommentDoesNotExistException::new);

        Comment savedReply = getSavedReply(request, member, parentComment);
        ReplyInfoResponse response = ReplyInfoResponse.from(savedReply);

        //댓글 작성자가 자신이 아닐 경우에, 작성자에게 알림을 전송합니다
        if (!replyComment.getMember().getId().equals(member.getId())) {
            notifyOriginalReplyAuthor(replyComment, member, savedReply.getContent());
        }
        return response;
    }

    @Transactional
    public CommentInfoResponse updateComment(String token, CommentRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(CommentDoesNotExistException::new);
        comment.updateFields(request);
        commentRepository.save(comment);
        Long memberId = tokenProvider.getMemberFromToken(token).getId();
        CommentInfoResponse response = CommentInfoResponse.from(comment);
        if(likeRepository.findByPostIdAndMemberIdAndCommentId(response
                .getPostId(), memberId, response.getCommentId()).isPresent()) {
            response.setCommentLike(true);
        }
        return response;
    }

    @Transactional
    public void deletePost(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);
        commentRepository.save(comment);
        commentRepository.delete(comment);
    }

    @Transactional
    public List<CommentInfoResponse> searchComments(String token, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);
        List<Comment> comments = commentRepository.findByPostAndReplyFlagIsFalse(post);
        List<CommentInfoResponse> responseList = comments.stream()
                .map(CommentInfoResponse::from).toList();
        Long memberId = tokenProvider.getMemberFromToken(token).getId();
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            CommentInfoResponse response = responseList.get(i);
            Set<LikeInfoResponse> likeSet = likeRepository.findByComment(comment)
                    .stream().map(LikeInfoResponse::from)
                    .collect(Collectors.toSet());
            response.setLikeCount(likeSet.size());
            if(likeRepository.findByPostIdAndMemberIdAndCommentId(response
                    .getPostId(), memberId, response.getCommentId()).isPresent()) {
                response.setCommentLike(true);
            }
        }

        return responseList;
    }

    @Transactional
    public List<ReplyInfoResponse> searchReply(String token, Long parentCommentId) {
        Comment comment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentDoesNotExistException::new);
        return getReplyInfoResponses(token, comment);
    }

    private List<ReplyInfoResponse> getReplyInfoResponses(String token, Comment comment) {
        List<Comment> replies = commentRepository.findByParentComment(comment);
        List<ReplyInfoResponse> responseList = replies.stream()
                .map(ReplyInfoResponse::from).toList();
        Long memberId = tokenProvider.getMemberFromToken(token).getId();
        for (int i = 0; i < replies.size(); i++) {
            Comment reply = replies.get(i);
            ReplyInfoResponse response = responseList.get(i);
            Set<LikeInfoResponse> likeSet = likeRepository.findByComment(reply)
                    .stream().map(LikeInfoResponse::from)
                    .collect(Collectors.toSet());
            response.setLikeCount(likeSet.size());
            if(likeRepository.findByPostIdAndMemberIdAndCommentId(response
                    .getPostId(), memberId, response.getCommentId()).isPresent()) {
                response.setCommentLike(true);
            }
        }
        return responseList;
    }

    private Comment getSavedReply(CommentRequest request, Member member, Comment parentComment) {
        List<String> mentionList = new ArrayList<>();
        if (request.getMentionList() != null) {
            mentionList = request.getMentionList();
        }
        Comment reply = Comment.builder()
                .post(parentComment.getPost())
                .member(member)
                .content(request.getContents())
                .parentComment(parentComment)
                .replyFlag(true)
                .mentionList(mentionList)
                .build();
        Comment savedReply = commentRepository.save(reply);
        parentComment.getChildComments().add(reply);
        commentRepository.save(parentComment);
        return savedReply;
    }
    private void notifyMentionedMembers(List<String> mentionList, Member commenter, Post post, String commentContent) {
        for (String targetMember : mentionList) {
            Member friend = memberRepository.findByNickname(targetMember);
            notificationService.notify(notificationService.createNotifyDto(
                    friend,
                    post,
                    commenter,
                    commenter.getNickname() + "님이 댓글에서 회원님을 언급했습니다: " + commentContent
            ));
        }
    }

    private void notifyPostAuthor(Post post, Member commenter, String commentContent) {
        notificationService.notify(notificationService.createNotifyDto(
                post.getMember(),
                post,
                commenter,
                commenter.getNickname() + "님이 회원님의 게시물에 댓글을 남겼습니다: " + commentContent
        ));
    }

    private void notifyOriginalCommentAuthor(Comment commentAuthor, Member commenter, String replyContent) {
        notificationService.notify(notificationService.createNotifyDto(
                commentAuthor.getMember(),
                commentAuthor.getPost(),
                commenter,
                commenter.getNickname() + "님이 회원님의 댓글에 댓글을 남겼습니다: " + replyContent
        ));
    }

    private void notifyOriginalReplyAuthor(Comment replyComment, Member commenter, String replyContent) {
        notificationService.notify(notificationService.createNotifyDto(
                replyComment.getMember(),
                replyComment.getParentComment().getPost(),
                commenter,
                commenter.getNickname() + "님이 회원님의 댓글에 댓글을 남겼습니다: " + replyContent
        ));
    }
}