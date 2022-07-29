package com.monkeypenthouse.core.controller.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCheckExistResI {
    private final boolean result;
    private final String token;
}
