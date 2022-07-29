package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.service.dto.user.UserCheckAuthReqS;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public interface MessageService {
    void sendAuthNum(final String phoneNum) throws CoolsmsException;
    boolean checkAuthNum(final UserCheckAuthReqS params);
}
