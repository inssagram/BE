package com.be.inssagram.domain.like.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like-post")
    public ApiResponse<?> onLikePost(
            @RequestParam(value = "post-id") Long postId,
            @RequestParam(value = "member-id") Long memberId
            ) {
        likeService.onLikePost(postId, memberId);
        return ApiResponse.createSuccessWithNoContent();
    }

    @PostMapping("/like-comment")
    public ApiResponse<?> onLikeComment(
            @RequestParam(value = "comment-id") Long commentId,
            @RequestParam(value = "member-id") Long memberId
    ) {
        likeService.onLikeComment(commentId, memberId);
        return ApiResponse.createSuccessWithNoContent();
    }
}
