package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.controller.dto.user.UserGetTokenResI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserGetTokenResS {
    private final String grantType;
    private final String accessToken;
    private final Long accessTokenExpiresIn;
    private final String refreshToken;
    private final Long refreshTokenExpiresIn;

    public UserGetTokenResI toI() {
        return new UserGetTokenResI(
                grantType, accessToken, accessTokenExpiresIn, refreshToken, refreshTokenExpiresIn
        );
    }
}
