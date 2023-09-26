package com.be.inssagram.exception.member;

public class WrongAuthInfoException extends RuntimeException{
    public WrongAuthInfoException(){
        super("발급된 인증번호와 이메일이 불일치 합니다");
    }
}
