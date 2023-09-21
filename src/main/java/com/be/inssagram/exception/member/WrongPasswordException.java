package com.be.inssagram.exception.member;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(){
        super("입력하신 비밀번호가 정확하지 않습니다");
    }
}
