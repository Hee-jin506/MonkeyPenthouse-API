package com.monkeypenthouse.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// OncePerRequestFilter : 사용자의 요청 하나 당 딱 한번만 실행되는 필터
// Spring security의 인증 권한 제어 기능은 RequestDispatcher 클래스에 의해
// 다른 서블릿으로 dispatch되므로, 이때 이동할 서블릿에 도착하기전에 다시 한번 filter chain을 거친다.
// 따라서 그냥 Filter, GenericFilterBean으로 구현된 filter로 정의하면 필터가 두번 실행되는 현상 발생 가능
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    // 실제 필터링 로직은 dofilterInternal에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header에서 토큰을 꺼냄
        String jwt = resolveToken(request);

        // 2. validateToken으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext에 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, request)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 꺼내기
    private String resolveToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                return bearerToken.substring(7);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
