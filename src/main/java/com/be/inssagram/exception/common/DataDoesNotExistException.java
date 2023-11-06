package com.be.inssagram.exception.common;

public class DataDoesNotExistException extends RuntimeException{
    public DataDoesNotExistException() {
        super("존재하지 않은 정보입니다, 다시 확인후 시도해보십시오");
    }
}
