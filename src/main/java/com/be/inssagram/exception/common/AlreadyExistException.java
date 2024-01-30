package com.be.inssagram.exception.common;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(){
        super("이미 등록된 정보 입니다, 다시 확인후 시도해주세요");
    }
}
