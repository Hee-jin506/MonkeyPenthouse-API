package com.monkeypenthouse.core.controller.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserGetTokenResI {
    private final String grantType;
    private final String accessToken;
    private final Long accessTokenExpiresIn;
    private final String refreshToken;
    private final Long refreshTokenExpiresIn;
}
