package com.be.inssagram.exception.member;

import static com.be.inssagram.exception.errorCode.ErrorCode.USER_NOT_MATCH;

public class UserNotMatchException extends RuntimeException {
    public UserNotMatchException() {
        super(USER_NOT_MATCH.getDescription());
    }
}
