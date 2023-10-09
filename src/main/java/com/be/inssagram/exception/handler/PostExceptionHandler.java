package com.be.inssagram.exception.handler;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostExceptionHandler {

    //포스트 조회시 없는 포스트일 때
    @ExceptionHandler(PostDoesNotExistException.class)
    public ResponseEntity<ApiResponse<?>> handlePostDoesNotExistException(
            RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.createError(exception.getMessage()));
    }
}
