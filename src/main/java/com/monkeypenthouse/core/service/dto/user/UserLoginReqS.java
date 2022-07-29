package com.monkeypenthouse.core.service.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class UserLoginReqS {
    private final String email;
    private final String password;
}
