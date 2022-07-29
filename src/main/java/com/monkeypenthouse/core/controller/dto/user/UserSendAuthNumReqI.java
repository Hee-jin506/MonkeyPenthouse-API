package com.monkeypenthouse.core.controller.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class UserSendAuthNumReqI {
    @Pattern(regexp = "^\\d{9,11}$")
    private String phoneNum;
}
