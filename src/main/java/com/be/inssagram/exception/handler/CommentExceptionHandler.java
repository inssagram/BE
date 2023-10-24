package com.be.inssagram.exception.handler;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.exception.comment.CannotCreateCommentException;
import com.be.inssagram.exception.comment.CommentDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentExceptionHandler {

    // 댓글 조회시 없는 댓글일 때
    @ExceptionHandler(CommentDoesNotExistException.class)
    public ResponseEntity<ApiResponse<?>> handleCommentDoesNotExistException(
            RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.createError(exception.getMessage()));
    }

    // 더이상 댓글을 달 수 없을 때
    @ExceptionHandler(CannotCreateCommentException.class)
    public ResponseEntity<ApiResponse<?>> handleCannotCreateCommentException(
            IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
                .body(ApiResponse.createError(exception.getMessage()));
    }


}
