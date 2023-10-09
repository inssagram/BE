package com.be.inssagram.exception.comment;

import static com.be.inssagram.exception.errorCode.ErrorCode.COMMENT_NOT_FOUND;

public class CommentDoesNotExistException extends RuntimeException {
    public CommentDoesNotExistException() {
        super(COMMENT_NOT_FOUND.getDescription());
    }
}
