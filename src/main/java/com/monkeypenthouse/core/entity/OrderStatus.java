package com.monkeypenthouse.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    IN_PROGRESS, COMPLETED, CANCELLED, RESERVED;

    @JsonCreator
    public static OrderStatus from(String s) {
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
