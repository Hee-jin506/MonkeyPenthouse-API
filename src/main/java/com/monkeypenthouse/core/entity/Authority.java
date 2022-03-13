package com.monkeypenthouse.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Authority {
    USER, ADMIN, GUEST;

    @JsonCreator
    public static Authority from(String s) {
        return Authority.valueOf(s.toUpperCase());
    }
}
