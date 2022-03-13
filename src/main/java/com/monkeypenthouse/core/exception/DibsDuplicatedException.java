package com.monkeypenthouse.core.exception;

import org.springframework.http.HttpStatus;

public class DibsDuplicatedException extends RuntimeException{

    public DibsDuplicatedException() {
        super("해당 찜하기 정보가 이미 존재합니다.");
    }
}
