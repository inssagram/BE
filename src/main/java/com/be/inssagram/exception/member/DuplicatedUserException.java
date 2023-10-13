package com.be.inssagram.exception.member;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException() {
        super("입력하신 이메일로 등록된 유저가 존재합니다");
    }
}
