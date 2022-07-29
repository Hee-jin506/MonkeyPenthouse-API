package com.monkeypenthouse.core.controller.dto.user;

import com.monkeypenthouse.core.service.dto.user.UserCheckAuthReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCheckAuthReqI {
    private String phoneNum;
    private String authNum;

    public UserCheckAuthReqS toS() {
        return new UserCheckAuthReqS(phoneNum, authNum);
    }
}
