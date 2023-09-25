package com.be.inssagram.exception.handler;


import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.exception.member.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    //회원가입시 중복 체크
    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    //로그인시 이메일정보가 틀렸을때
    @ExceptionHandler(WrongEmailException.class)
    public ResponseEntity<ApiResponse<?>> handleWrongInfoException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    //유저 조회시 없는 유저일때
    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ApiResponse<?>> handleUserDoesNotExistException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    //로그인시 비밀번호가 틀렸을때
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ApiResponse<?>> handleWrongPasswordException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    //회원정보의 비밀번호를 수정할때 비밀번호가 같을시
    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ApiResponse<?>> handleSamePasswordException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }
}
