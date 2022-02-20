package com.monkeypenthouse.core.security;

import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class SecurityUtil {

    // SecurityContext에 세팅한 유저 정보를 꺼냄
    // userId 를 Long 타입으로 저장했으므로 Long 타입으로 파싱하여 반환
    public static String getCurrentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        return authentication.getName();
    }
}
