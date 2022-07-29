package com.monkeypenthouse.core.controller.dto.user;

import com.monkeypenthouse.core.repository.entity.LoginType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserGetEmailByPhoneNumResI {
    private final Long id;
    private final String email;
    private final LoginType loginType;
}
