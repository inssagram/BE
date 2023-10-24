package com.be.inssagram.exception.post;

import static com.be.inssagram.exception.errorCode.ErrorCode.POST_NOT_FOUND;

public class PostDoesNotExistException extends RuntimeException {
    public PostDoesNotExistException() {
        super(POST_NOT_FOUND.getDescription());
    }
}
