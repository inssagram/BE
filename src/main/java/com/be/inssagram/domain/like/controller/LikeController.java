package com.be.inssagram.domain.like.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post")
    public ApiResponse<?> onLikePost(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "post-id") Long postId
    ) {
        return ApiResponse.createMessage(likeService.onLikePost(token, postId));
    }

    @PostMapping("/comment")
    public ApiResponse<?> onLikeComment(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "comment-id") Long commentId
    ) {
        return ApiResponse.createMessage(
                likeService.onLikeComment(token, commentId));
    }

    @PostMapping("/story")
    public ApiResponse<?> onLikeStory(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "story-id") Long storyId
    ) {
        return ApiResponse.createMessage(
                likeService.onLikeStory(token, storyId));
    }

    @GetMapping("/member-list/post")
    public ApiResponse<?> searchMemberLikePost(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "post-id") Long postId
    ) {
        return ApiResponse.createSuccess(
                likeService.searchMemberLikePost(token, postId));
    }

    @GetMapping("/member-list/comment")
    public ApiResponse<?> searchMemberLikeComment(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "comment-id") Long commentId
    ) {
        return ApiResponse.createSuccess(
                likeService.searchMemberLikeComment(token, commentId));
    }

}
