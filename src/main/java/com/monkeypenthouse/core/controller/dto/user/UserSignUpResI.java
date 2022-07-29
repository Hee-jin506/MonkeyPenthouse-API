package com.monkeypenthouse.core.controller.dto.user;

import com.monkeypenthouse.core.repository.entity.Authority;
import com.monkeypenthouse.core.repository.entity.LifeStyle;
import com.monkeypenthouse.core.repository.entity.LoginType;
import com.monkeypenthouse.core.repository.entity.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserSignUpResI {
    private final Long id;
    private final String name;
    private final LocalDate birth;
    private final int gender;
    private final String email;
    private final String phoneNum;
    private final String roomId;
}
