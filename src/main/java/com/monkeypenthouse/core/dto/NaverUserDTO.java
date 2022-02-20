package com.monkeypenthouse.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NaverUserDTO {

    private String resultcode;
    private String message;
    private Response response;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Response {
        private String id;
        private String name;
        private String gender;
        private String email;
        private String mobile;
        private String mobile_e164;
    }

}
