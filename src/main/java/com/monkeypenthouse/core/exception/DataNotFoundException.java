package com.monkeypenthouse.core.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public class DataNotFoundException extends ExpectedException {

    private final Object target;

    public DataNotFoundException(Object target) {
        super(HttpStatus.NOT_FOUND, "해당 정보(" + target.getClass().getSimpleName() + ")가 존재하지 않습니다.");
        this.target = target;
    }

    public Optional<Object> getRequestedTarget() {
        return Optional.ofNullable(target);
    }
}
