package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.controller.dto.user.UserGetEmailByPhoneNumResI;
import com.monkeypenthouse.core.repository.entity.LoginType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserGetEmailByPhoneNumResS {
    private final Long id;
    private final String email;
    private final LoginType loginType;

    public UserGetEmailByPhoneNumResI toI() {
        return new UserGetEmailByPhoneNumResI(id, email, loginType);
    }
}
