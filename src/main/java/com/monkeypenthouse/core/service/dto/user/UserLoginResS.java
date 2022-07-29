package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.controller.dto.user.UserLoginResI;
import com.monkeypenthouse.core.repository.entity.LifeStyle;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Builder
public class UserLoginResS {
    private final Long id;
    private final String name;
    private final LocalDate birth;
    private final int gender;
    private final String email;
    private final String phoneNum;
    private final String roomId;
    private final LifeStyle lifeStyle;
    private final String grantType;
    private final String accessToken;
    private final Long accessTokenExpiresIn;
    private final String refreshToken;
    private final Long refreshTokenExpiresIn;

    public UserLoginResI toI() {
        return new UserLoginResI(
                id,
                name,
                birth,
                gender,
                email,
                phoneNum,
                roomId,
                grantType,
                lifeStyle,
                accessToken,
                accessTokenExpiresIn,
                refreshToken,
                refreshTokenExpiresIn
        );
    }
}
