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
            @RequestBody CommentRequest request) {
        return ApiResponse.createSuccess(
                commentService.createComment(token, request));
    }

    @PostMapping("/create/reply")
    public ApiResponse<ReplyInfoResponse> createReply(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentRequest request) {
        return ApiResponse.createSuccess(
                commentService.createReply(token, request));
    }

    @PostMapping("/create/replytoreply")
    public ApiResponse<ReplyInfoResponse> createReplyToReply(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentRequest request) {
        return ApiResponse.createSuccess(
                commentService.createReplyToReply(token, request));
    }

    @PutMapping("/update")
    public ApiResponse<CommentInfoResponse> updateComment(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentRequest request) {
        return ApiResponse.createSuccessWithMessage(
                commentService.updateComment(token, request), "수정됨");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deleteComment(
            @PathVariable Long id) {
        commentService.deletePost(id);
        return ApiResponse.createMessage("삭제 완료");
    }

    @GetMapping("/search")
    public ApiResponse<List<CommentInfoResponse>> searchParentComments2(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "post-id") Long postId
    ) {
        return ApiResponse.createSuccess(
                commentService.searchComments(token, postId));
    }

    @GetMapping("/search/reply")
    public ApiResponse<List<ReplyInfoResponse>> searchReplyByParentComment2(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "parent-comment-id") Long parentCommentId
    ) {
        return ApiResponse.createSuccess(
                commentService.searchReply(token, parentCommentId));
    }
}
