package com.be.inssagram.exception.member;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException() {
        super("존재하지 않는 유저입니다");
    }
}
