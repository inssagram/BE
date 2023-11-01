package com.be.inssagram.domain.tag.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/tagged-post-list")
    public ApiResponse<?> searchPostWithTaggedMemberId(
            @RequestParam(value = "tagged-member-id") Long memberId
    ) {
        return ApiResponse.createSuccess(tagService
                .searchPostWithMemberId(memberId));
    }

}
