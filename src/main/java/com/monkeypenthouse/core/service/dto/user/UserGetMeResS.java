package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.controller.dto.user.UserGetMeResI;
import com.monkeypenthouse.core.repository.entity.Authority;
import com.monkeypenthouse.core.repository.entity.LifeStyle;
import com.monkeypenthouse.core.repository.entity.LoginType;
import com.monkeypenthouse.core.repository.entity.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Builder
public class UserGetMeResS {
    private final Long id;
    private final String name;
    private final LocalDate birth;
    private final int gender;
    private final String email;
    private final String phoneNum;
    private final String roomId;
    private final Authority authority;
    private final LoginType loginType;
    private final LifeStyle lifeStyle;

    public UserGetMeResI toI() {
        return new UserGetMeResI(
                id, name, birth, gender, email, phoneNum, roomId, authority, loginType, lifeStyle
        );
    }
}
