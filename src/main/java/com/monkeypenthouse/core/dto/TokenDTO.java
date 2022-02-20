package com.monkeypenthouse.core.dto;

import lombok.*;


public class TokenDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueReqDTO extends TokenDTO {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueResDTO extends TokenDTO {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpiresIn;
        private String refreshToken;
        private Long refreshTokenExpiresIn;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoResDTO extends TokenDTO {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverResDTO extends TokenDTO {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String error;
        private String error_description;
    }
}