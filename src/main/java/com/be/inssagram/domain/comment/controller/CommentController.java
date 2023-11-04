package com.be.inssagram.domain.comment.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.comment.dto.request.CommentRequest;
import com.be.inssagram.domain.comment.dto.response.CommentInfoResponse;
import com.be.inssagram.domain.comment.dto.response.ReplyInfoResponse;
import com.be.inssagram.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ApiResponse<CommentInfoResponse> createComment(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "post-id") Long postId,
            @RequestBody CommentRequest request) {
        return ApiResponse.createSuccess(
                commentService.createComment(token, postId, request));
    }

    @PostMapping("/create/reply")
    public ApiResponse<ReplyInfoResponse> createReply(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "parent-comment-id") Long parentCommentId,
            @RequestBody CommentRequest request) {
        return ApiResponse.createSuccess(
                commentService.createReply(token, parentCommentId, request));
    }

    @PostMapping("/create/reply/{replyId}")
    public ApiResponse<ReplyInfoResponse> createReplyToReply(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "parent-comment-id") Long parentCommentId,
            @RequestBody CommentRequest request, @PathVariable Long replyId) {
        return ApiResponse.createSuccess(
                commentService.createReplyToReply(token, parentCommentId, replyId, request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CommentInfoResponse> updateComment(
            @PathVariable Long id, @RequestBody CommentRequest request) {
        return ApiResponse.createSuccessWithMessage(
                commentService.updateComment(id, request), "수정됨");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deleteComment(
            @PathVariable Long id) {
        commentService.deletePost(id);
        return ApiResponse.createMessage("삭제 완료");
    }

    @GetMapping("/search")
    public ApiResponse<List<CommentInfoResponse>> searchParentComments2(
            @RequestParam(value = "post-id") Long postId
    ) {
        return ApiResponse.createSuccess(
                commentService.searchComments(postId));
    }

    @GetMapping("/search/reply")
    public ApiResponse<List<ReplyInfoResponse>> searchReplyByParentComment2(
            @RequestParam(value = "parent-comment-id") Long parentCommentId
    ) {
        return ApiResponse.createSuccess(
                commentService.searchReply(parentCommentId));
    }
}
