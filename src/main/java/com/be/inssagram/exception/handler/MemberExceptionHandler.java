package com.be.inssagram.exception.handler;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.exception.member.DuplicatedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }
}
