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
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.comment.CannotCreateCommentException;
import com.be.inssagram.exception.comment.CommentDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;

    private final TokenProvider tokenProvider;

    public CommentInfoResponse createComment(
            String token, Long postId, CommentRequest request) {
        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        //멘션 리스트가 비어있을 때를 처리합니다.
        List<String> mentionList = new ArrayList<>();
        if (request.getMentionList() != null) {
            mentionList = request.getMentionList();
        }
        // 댓글(Comment) 객체를 생성합니다.
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(request.getContents())
                .mentionList(mentionList)
                .build();

        CommentInfoResponse response = CommentInfoResponse.from(
                commentRepository.save(comment));

        //게시물 작성자가 자신이 아닐 경우에, 작성자에게 알림을 전송합니다
        if (!post.getMember().getId().equals(member.getId())) {
            notificationService.notify(notificationService
                    .createNotifyDto(
                            post.getMember().getId(),
                            "post",
                            post.getId(),
                            post.getImage().get(0),
                            member.getId(),
                            member.getNickname(),
                            member.getProfilePic(),
                            member.getNickname() + "님이 회원님의 게시물에 댓글을 다셧습니다"
                    ));
        }
        // 댓글을 저장합니다.
        return response;
    }

    public ReplyInfoResponse createReply(
            String token, Long parentCommentId, CommentRequest request) {
        // 유저(Member)를 찾아옵니다.
        Member member = tokenProvider.getMemberFromToken(token);

        // 부모 댓글을 찾아옵니다.
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentDoesNotExistException::new);

        // isReply 값 확인
        if (parentComment.isReplyFlag()) {
            throw new CannotCreateCommentException();
        }

        // 대댓글을 생성합니다.
        Comment savedReply = getSavedReply(request, member, parentComment);
        ReplyInfoResponse response = ReplyInfoResponse.from(savedReply);

        //댓글 작성자가 자신이 아닐 경우에, 작성자에게 알림을 전송합니다
        if (!parentComment.getMember().getId().equals(member.getId()))
            notificationService.notify(notificationService
                    .createNotifyDto(
                            parentComment.getMember().getId(),
                            "post",
                            parentComment.getPost().getId(),
                            parentComment.getPost().getImage().get(0),
                            member.getId(),
                            member.getNickname(),
                            member.getProfilePic(),
                            member.getNickname() + "님이 회원님의 댓글에 답장하였습니다"
                    ));
        // 대댓글을 저장합니다.
        return response;
    }

    public ReplyInfoResponse createReplyToReply(
            String token, Long parentCommentId, Long replyId, CommentRequest request
    ) {
        Member member = tokenProvider.getMemberFromToken(token);

        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentDoesNotExistException::new);

        Comment replyComment = commentRepository.findById(replyId)
                .orElseThrow(CommentDoesNotExistException::new);

        Comment savedReply = getSavedReply(request, member, parentComment);
        ReplyInfoResponse response = ReplyInfoResponse.from(savedReply);

        //댓글 작성자가 자신이 아닐 경우에, 작성자에게 알림을 전송합니다
        if (!replyComment.getMember().getId().equals(member.getId())) {
            notificationService.notify(notificationService
                    .createNotifyDto(
                            replyComment.getMember().getId(),
                            "post",
                            parentComment.getPost().getId(),
                            parentComment.getPost().getImage().get(0),
                            member.getId(),
                            member.getNickname(),
                            member.getProfilePic(),
                            member.getNickname() + "님이 회원님의 댓글에 답장하였습니다"
                    ));
        }
        return response;
    }

    public CommentInfoResponse updateComment(
            Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        comment.updateFields(request);
        commentRepository.save(comment);
        return CommentInfoResponse.from(comment);
    }

    public void deletePost(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);
        commentRepository.save(comment);
        commentRepository.delete(comment);
    }

    public List<CommentInfoResponse> searchComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        List<Comment> comments = commentRepository.findByPostAndReplyFlagIsFalse(post);
        List<CommentInfoResponse> responseList = comments.stream()
                .map(CommentInfoResponse::from).toList();

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            CommentInfoResponse response = responseList.get(i);
            Set<LikeInfoResponse> likeSet = likeRepository.findByComment(comment)
                    .stream().map(LikeInfoResponse::from)
                    .collect(Collectors.toSet());
            response.setLikeCount(likeSet.size());
        }

        return responseList;
    }

    public List<ReplyInfoResponse> searchReply(Long parentCommentId) {
        Comment comment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentDoesNotExistException::new);

        return getReplyInfoResponses(comment);
    }

    private List<ReplyInfoResponse> getReplyInfoResponses(Comment comment) {
        List<Comment> replies = commentRepository.findByParentComment(comment);
        List<ReplyInfoResponse> responseList = replies.stream()
                .map(ReplyInfoResponse::from).toList();

        for (int i = 0; i < replies.size(); i++) {
            Comment reply = replies.get(i);
            ReplyInfoResponse response = responseList.get(i);
            Set<LikeInfoResponse> likeSet = likeRepository.findByComment(reply)
                    .stream().map(LikeInfoResponse::from)
                    .collect(Collectors.toSet());
            response.setLikeCount(likeSet.size());
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

}