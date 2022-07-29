package com.monkeypenthouse.core.service.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCheckExistReqS {
    private final String phoneNum;
    private final String email;
}
