package com.monkeypenthouse.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoUserDTO {

    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Properties {
        private String nickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class KakaoAccount {
        private boolean profile_nickname_needs_agreement;
        private Profile profile;
        private boolean has_email;
        private boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String  email;
        private boolean has_gender;
        private boolean gender_needs_agreement;
        private String gender;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Profile {
        private String nickname;
    }
}
