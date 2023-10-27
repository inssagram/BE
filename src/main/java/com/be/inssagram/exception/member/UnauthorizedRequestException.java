package com.be.inssagram.exception.member;

public class UnauthorizedRequestException extends RuntimeException{
    public UnauthorizedRequestException () {
        super("권한이 없는 요청입니다");
    }
}
