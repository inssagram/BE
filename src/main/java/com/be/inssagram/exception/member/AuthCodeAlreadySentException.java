package com.be.inssagram.exception.member;

public class AuthCodeAlreadySentException extends RuntimeException {
    public AuthCodeAlreadySentException(){
        super("이미 인증번호가 발급되었습니다, 이메일을 확인해주세요");
    }
}
