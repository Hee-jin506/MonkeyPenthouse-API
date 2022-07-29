package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.repository.entity.LoginType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserSignUpReqS {
    private final String name;
    private final LocalDate birth;
    private final int gender;
    private final String email;
    private final String password;
    private final String phoneNum;
    private final int infoReceivable;
    private final LoginType loginType;
}
