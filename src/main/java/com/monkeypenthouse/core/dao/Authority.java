package com.monkeypenthouse.core.dao;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Authority {
    USER, ADMIN;

    @JsonCreator
    public static Authority from(String s) {
        return Authority.valueOf(s.toUpperCase());
    }
}
