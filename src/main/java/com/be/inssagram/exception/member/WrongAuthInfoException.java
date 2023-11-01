package com.be.inssagram.exception.member;

public class WrongAuthInfoException extends RuntimeException{
    public WrongAuthInfoException(){
        super("인증번호가 불일치 합니다");
    }
}
