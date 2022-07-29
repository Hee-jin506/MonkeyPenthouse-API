package com.monkeypenthouse.core.connect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeypenthouse.core.controller.dto.user.UserNaverAuthResI;
import com.monkeypenthouse.core.controller.dto.user.UserNaverTokenResI;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class NaverConnector {
    @Value("${naver.authorization-grant-type}")
    private String AUTHORIZATION_GRANT_TYPE;
    @Value("${naver.client-id}")
    private String CLIENT_ID;
    @Value("${naver.client-secret}")
    private String CLIENT_SECRET;
    @Value("${naver.token-uri}")
    private String TOKEN_URI;
    @Value("${naver.user-info-uri}")
    private String USER_INFO_URI;

    public UserNaverTokenResI getToken(String code, String state) throws Exception {
        // 인증 코드를 갖고 토큰 받아오기
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", AUTHORIZATION_GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                TOKEN_URI,
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        ObjectMapper obMapper = new ObjectMapper();

        try {
            return obMapper.readValue(response.getBody(), UserNaverTokenResI.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("토큰 파싱 에러 : " + e.getMessage());
        }
    }

    public UserNaverAuthResI getUserInfo(String accessToken) throws Exception {
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

        return obMapper.readValue(response.getBody(), UserNaverAuthResI.class);

    }
}