package com.monkeypenthouse.core.controller.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserKakaoTokenResI {
    private final String access_token;
    private final String token_type;
    private final String refresh_token;
    private final int expires_in;
    private final String scope;
    private final int refresh_token_expires_in;
}
