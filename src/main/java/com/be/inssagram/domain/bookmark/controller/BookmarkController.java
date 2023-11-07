package com.be.inssagram.domain.bookmark.controller;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.bookmark.dto.request.BookmarkRequest;
import com.be.inssagram.domain.bookmark.dto.response.BookmarkResponse;
import com.be.inssagram.domain.bookmark.service.BookmarkService;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final TokenProvider tokenProvider;

    @GetMapping("/all")
    public ApiResponse<List<BookmarkResponse>> getBookmarkPosts(@RequestHeader(value = "Authorization") String token) {
        Long memberId = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token)).member_id();
        List<BookmarkResponse> results = bookmarkService.getAllBookmarkPosts(memberId);
        return ApiResponse.createSuccess(results);
    }

    @PostMapping("/save")
    public ApiResponse<?> saveBookmarkPost(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody BookmarkRequest request) {
        Long memberId = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token)).member_id();
        bookmarkService.bookmarkPost(request, memberId);
        return ApiResponse.createMessage("게시물을 북마크 하셧습니다");
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deleteBookmarkPost(@RequestHeader(value = "Authorization") String token,
                                             @RequestBody BookmarkRequest request) {
        Long memberId = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token)).member_id();
        bookmarkService.removeBookmark(request, memberId);
        return ApiResponse.createMessage("북마크를 해제하셧습니다");
    }
}
