package com.monkeypenthouse.core.service;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

public interface MessageService {
    void sendSMS(String phoneNum) throws CoolsmsException;
    boolean checkAuthNum(String phoneNum, String authNum) throws Exception;
}
