package com.monkeypenthouse.core.exception;

import org.springframework.http.HttpStatus;

public class ExpectedException extends Exception {

    private final HttpStatus httpStatus;
    private int detailStatus;

    public ExpectedException(HttpStatus statusCode, int detailStatus, String message) {
        super(message);
        this.httpStatus = statusCode;
        this.detailStatus = detailStatus;
    }

    public ExpectedException(HttpStatus statusCode, String message) {
        super(message);
        this.httpStatus = statusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public int getDetailStatus() {
        return detailStatus;
    }
}
