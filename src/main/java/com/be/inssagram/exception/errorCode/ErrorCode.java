package com.be.inssagram.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // member error code
    USER_NOT_MATCH("유저가 일치하지 않습니다. 유저정보를 확인해주세요."),

    // Post Error Code
    POST_NOT_FOUND("존재하지 않는 게시글 입니다."),

    // Comment Error Code
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다."),
    IMPOSSIBLE_CREATE_COMMENT("더이상 댓글을 달 수 없습니다.");
    private final String description;
}
