package com.be.inssagram.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Post Error Code
    POST_NOT_FOUND("존재하지 않는 게시글 입니다.")

    ;
    private final String description;
}
