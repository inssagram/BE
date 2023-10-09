package com.be.inssagram.exception.comment;

import static com.be.inssagram.exception.errorCode.ErrorCode.IMPOSSIBLE_CREATE_COMMENT;

public class CannotCreateCommentException extends IllegalArgumentException {
    public CannotCreateCommentException() {
        super(IMPOSSIBLE_CREATE_COMMENT.getDescription());
    }
}
