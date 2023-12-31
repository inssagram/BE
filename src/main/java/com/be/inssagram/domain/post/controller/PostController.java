package com.be.inssagram.domain.post.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ApiResponse<PostInfoResponse> createPost(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreatePostRequest request
    ) {
        PostInfoResponse response = postService.createPost(token, request);
        return ApiResponse.createSuccessWithMessage(response, "완료");
    }

    @PutMapping("/update/{id}")
    public ApiResponse<PostInfoResponse> updatePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id, @RequestBody UpdatePostRequest request) {
        return ApiResponse.createSuccess(postService.updatePost(token, id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deletePost(
            @PathVariable Long id) {
        postService.deletePost(id);
        return ApiResponse.createMessage("삭제 완료");
    }

    @GetMapping("/search-detail/{id}")
    public ApiResponse<PostInfoResponse> searchPostDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        return ApiResponse.createSuccess(postService.searchPostDetail(token, id));
    }

    @GetMapping("/all")
    public ApiResponse<List<PostInfoResponse>> searchPostAll(
            @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.createSuccess(postService.searchPostAll(token));
    }

    @GetMapping("/my")
    public ApiResponse<List<PostInfoResponse>> searchMyPosts(
            @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.createSuccess(postService.searchMyPosts(token));
    }

    @GetMapping("/following-member")
    public ApiResponse<List<PostInfoResponse>> searchPostsWithFollowingMember(
            @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.createSuccess(postService.searchPostsWithFollowingMember(token));
    }

    @GetMapping("/member")
    public ApiResponse<List<PostInfoResponse>> searchPostWithMemberId(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "member-id") Long memberId
    ) {
        return ApiResponse.createSuccess(
                postService.searchPostWithMember(token, memberId));
    }

}