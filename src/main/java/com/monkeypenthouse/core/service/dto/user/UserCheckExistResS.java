package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.controller.dto.user.UserCheckExistResI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserCheckExistResS {
    private final boolean result;
    private final String token;

    public UserCheckExistResI toI() {
        return new UserCheckExistResI(result, token);
    }
}
