package com.be.inssagram.domain.hashTag.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.hashTag.service.HashTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HashTagController {

    private final HashTagService hashTagService;

    @GetMapping("/hashTag-post-list")
    public ApiResponse<?> searchPostWithHashTagName(
            @RequestParam(value = "hashtag-name") String name
    ) {
        return ApiResponse.createSuccess(hashTagService
                .searchPostWithHashTagName(name));
    }
}
