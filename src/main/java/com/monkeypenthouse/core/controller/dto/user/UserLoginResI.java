package com.monkeypenthouse.core.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monkeypenthouse.core.repository.entity.LifeStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserLoginResI {
    private final Long id;
    private final String name;
    @JsonFormat(pattern = "yyyy.MM.dd")
    private final LocalDate birth;
    private final int gender;
    private final String email;
    private final String phoneNum;
    private final String roomId;
    private final String grantType;
    private final LifeStyle lifeStyle;
    private final String accessToken;
    private final Long accessTokenExpiresIn;
    private final String refreshToken;
    private final Long refreshTokenExpiresIn;


}
