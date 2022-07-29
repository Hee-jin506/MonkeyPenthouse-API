package com.monkeypenthouse.core.repository.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LoginType {
    LOCAL, KAKAO, APPLE, NAVER;

    @JsonCreator
    public static LoginType from(String s) {
        return LoginType.valueOf(s.toUpperCase());
    }
}
