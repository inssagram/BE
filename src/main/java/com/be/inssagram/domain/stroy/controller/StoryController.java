package com.be.inssagram.domain.stroy.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.follow.dto.response.FollowingList;
import com.be.inssagram.domain.stroy.dto.request.CreateStoryRequest;
import com.be.inssagram.domain.stroy.dto.response.StoryInfoResponse;
import com.be.inssagram.domain.stroy.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping("/story/create")
    public ApiResponse<StoryInfoResponse> createStory(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateStoryRequest request
    ) {
        StoryInfoResponse response = storyService.createStory(token, request);
        return ApiResponse.createSuccessWithMessage(response, "완료");
    }


    @GetMapping("/story/search-parentstory/member")
    public ApiResponse<StoryInfoResponse> searchParentStoryByMemberId(
            @RequestParam(value = "member-id") Long memberId
    ) {
        return ApiResponse.createSuccess(
                storyService.searchParentStoryByMemberId(memberId));
    }

    @GetMapping("/story/search-memberlist/member")
    public ApiResponse<List<FollowingList>> searchMemberHaveStory(
            @RequestParam(value = "member-id") Long memberId
    ) {
        return ApiResponse.createSuccess(
                storyService.searchMemberHaveStory(memberId));
    }

    @GetMapping("/story/search-childstory/member")
    public ApiResponse<List<StoryInfoResponse>> searchStoryWithChildStoryByMemberId(
            @RequestParam(value = "member-id") Long memberId
    ) {
        return ApiResponse.createSuccess(
                storyService.searchStoryWithChildStoryByMemberId(memberId));
    }

    @DeleteMapping("/story/delete/{storyId}")
    public ApiResponse<?> deleteStory(@PathVariable Long storyId
    ) {
        storyService.deleteStory(storyId);
        return ApiResponse.createSuccessWithNoContent();
    }
}
