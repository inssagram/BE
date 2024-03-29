package com.be.inssagram.exception.handler;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.exception.common.AlreadyExistException;
import com.be.inssagram.exception.common.DataDoesNotExistException;
import com.be.inssagram.exception.member.DuplicatedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    //없는정보 조회시 체크
    @ExceptionHandler(DataDoesNotExistException.class)
    public ResponseEntity<ApiResponse<?>> handleDataDoesNotExistException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.createError(exception.getMessage()));
    }

    //이미 등록된 정보 등록할시 처리
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyExistException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(exception.getMessage()));
    }
}
