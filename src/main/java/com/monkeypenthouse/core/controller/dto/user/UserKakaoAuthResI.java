package com.monkeypenthouse.core.controller.dto.user;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class UserKakaoAuthResI {

    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @RequiredArgsConstructor
    public static class Properties {
        private String nickname;
    }

    @Getter
    @RequiredArgsConstructor
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

    @Getter
    @RequiredArgsConstructor
    public static class Profile {
        private String nickname;
    }
}
