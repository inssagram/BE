package com.be.inssagram.exception.member;

public class WrongInfoException extends RuntimeException{
    public WrongInfoException(){
        super("입력하신 이메일 또는 비밀번호가 정확하지 않습니다");
    }
}
