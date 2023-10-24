package com.be.inssagram.exception.member;

public class WrongEmailException extends RuntimeException {
    public WrongEmailException() {
        super("입력하신 이메일이 정확하지 않습니다");
    }
}
