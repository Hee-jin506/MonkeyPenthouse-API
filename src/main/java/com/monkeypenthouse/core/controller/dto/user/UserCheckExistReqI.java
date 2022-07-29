package com.monkeypenthouse.core.controller.dto.user;

import com.monkeypenthouse.core.service.dto.user.UserCheckExistReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class UserCheckExistReqI {
    @Pattern(regexp = "^\\d{9,11}$")
    private final String phoneNum;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
    private final String email;

    public UserCheckExistReqS toS() {
        return new UserCheckExistReqS(phoneNum, email);
    }
}
