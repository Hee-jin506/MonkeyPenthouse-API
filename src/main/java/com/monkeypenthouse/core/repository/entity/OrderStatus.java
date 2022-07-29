package com.monkeypenthouse.core.repository.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    IN_PROGRESS, COMPLETED, CANCELLED, RESERVED, CANCEL_FAILED;

    @JsonCreator
    public static OrderStatus from(String s) {
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
