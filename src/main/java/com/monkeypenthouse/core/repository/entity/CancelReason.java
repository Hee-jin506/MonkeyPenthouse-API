package com.monkeypenthouse.core.repository.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CancelReason {
    EVENT_CANCELLED("이벤트 취소"), CUSTOMER_REMORSE("고객 변심");
    private final String value;
    CancelReason(String value) { this.value = value; }

    @JsonCreator
    public static CancelReason from(String s) {
        return CancelReason.valueOf(s);
    }

    public String value() { return value; }
}