package com.be.inssagram.exception.member;

public class SamePasswordException extends RuntimeException{
    public SamePasswordException(){
        super("기존 비밀번호와 일치합니다. 새로운 비밀번호를 입력하세요");
    }
}
