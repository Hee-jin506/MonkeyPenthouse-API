package com.monkeypenthouse.core.controller.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserNaverTokenResI {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String error;
    private String error_description;
}
