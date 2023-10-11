package com.be.inssagram.domain.comment.service;

import com.be.inssagram.domain.comment.dto.request.CommentRequest;
import com.be.inssagram.domain.comment.dto.response.CommentInfoResponse;
import com.be.inssagram.domain.comment.dto.response.ReplyInfoResponse;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.comment.repository.CommentRepository;
import com.be.inssagram.domain.like.dto.response.LikeInfoResponse;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.comment.CannotCreateCommentException;
import com.be.inssagram.exception.comment.CommentDoesNotExistException;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.be.inssagram.exception.member.UserNotMatchException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public CommentInfoResponse createComment(
            Long postId, CommentRequest request) {
        // 유저(Member)를 찾아옵니다.
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(UserDoesNotExistException::new);

        // 게시물(Post)을 찾아옵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        // 댓글(Comment) 객체를 생성합니다.
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(request.getContents())
                .build();

        // 댓글을 저장합니다.
        return CommentInfoResponse.from(commentRepository.save(comment));
    }

    public ReplyInfoResponse createReply(
            Long parentCommentId, CommentRequest request) {
        // 유저(Member)를 찾아옵니다.
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(UserDoesNotExistException::new);

        // 부모 댓글을 찾아옵니다.
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentDoesNotExistException::new);

        // isReply 값 확인
        if (parentComment.isReplyFlag()) {
            throw new CannotCreateCommentException();
        }

        // 대댓글을 생성합니다.
        Comment reply = Comment.builder()
                .post(parentComment.getPost())
                .member(member)
                .content(request.getContents())
                .parentComment(parentComment)
                .replyFlag(true)
                .build();
        Comment savedReply = commentRepository.save(reply);
        parentComment.getChildComments().add(reply);
        commentRepository.save(parentComment);
        // 대댓글을 저장합니다.
        return ReplyInfoResponse.from(savedReply);
    }

    public CommentInfoResponse updateComment(
            Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentDoesNotExistException::new);

        if (!Objects.equals(comment.getMember().getId(), request.getMemberId())) {
            throw new UserNotMatchException();
        }

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

    public List<CommentInfoResponse> searchParentComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);
//        return commentRepository.findParentCommentsByPost(post).stream()
//                .map(CommentInfoResponse::from).toList();
        return commentRepository.findParentCommentsByPostAndIsReply(post);
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
            response.setLikedByPerson(likeSet);
            response.setLikeCount(likeSet.size());
            response.setChildComments(getReplyInfoResponses(comment));
        }

        return responseList;
    }

    public List<CommentInfoResponse> searchReplyByParentComment(Long parentCommentId) {
        Comment comment = commentRepository.findById(parentCommentId)
                .orElseThrow(CommentDoesNotExistException::new);
        return commentRepository.findRepliesByParentComment(comment);
    }

    public List<ReplyInfoResponse> searchReply (Long parentCommentId) {
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
            response.setLikedByPerson(likeSet);
            response.setLikeCount(likeSet.size());
        }
        return responseList;
    }

}

