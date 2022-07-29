package com.monkeypenthouse.core.service.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCheckAuthReqS {
    private final String phoneNum;
    private final String authNum;
}
