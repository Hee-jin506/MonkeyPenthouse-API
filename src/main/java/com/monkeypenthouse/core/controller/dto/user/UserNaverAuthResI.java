package com.monkeypenthouse.core.controller.dto.user;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class UserNaverAuthResI {

    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private String gender;
        private String email;
        private String mobile;
        private String mobile_e164;
    }

}
