package com.monkeypenthouse.core.connect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeypenthouse.core.dto.KakaoUserDTO;
import com.monkeypenthouse.core.dto.TokenDTO.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class KakaoConnector {
    @Value("${kakao.authorization-grant-type}")
    private String AUTHORIZATION_GRANT_TYPE;
    @Value("${kakao.client-id}")
    private String CLIENT_ID;
    @Value("${kakao.client-secret}")
    private String CLIENT_SECRET;
    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${kakao.token-uri}")
    private String TOKEN_URI;
    @Value("${kakao.user-info-uri}")
    private String USER_INFO_URI;

    public KakaoResDTO getToken(String code) throws Exception {
        // 인증 코드를 갖고 토큰 받아오기
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", AUTHORIZATION_GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                TOKEN_URI,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper obMapper = new ObjectMapper();

        try {
            return obMapper.readValue(response.getBody(), KakaoResDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("토큰 파싱 에러 : " + e.getMessage());
        }
    }

    public KakaoUserDTO getUserInfo(String accessToken) throws Exception {
        // 회원정보 받아오기
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> getUserInfoRequest = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange(
                USER_INFO_URI,
                HttpMethod.POST,
                getUserInfoRequest,
                String.class
        );
        ObjectMapper obMapper = new ObjectMapper();

        return obMapper.readValue(response.getBody(), KakaoUserDTO.class);
    }
}
