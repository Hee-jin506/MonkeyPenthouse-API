package com.monkeypenthouse.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LifeStyle {
    ADVENTURER, INVENTOR, DAYDREAMER, SOCIALIZER, OPTIMIST, LEADER, GUARDIAN, ARTIST;

    @JsonCreator
    public static LifeStyle from(String s) {
        return LifeStyle.valueOf(s.toUpperCase());
    }
}
