package com.monkeypenthouse.core.security;

import com.monkeypenthouse.core.dao.Tokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    // accessToken 만료 시간
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 15;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60; // 연동 테스트를 위해 1분으로 변경
    // refreshToken 만료 시간
//    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 2; // 연동 테스트를 위해 2분으로 변경

    private final Key key;

    // HMAC-SHA 알고리즘을 이용하여 특정 문자열을 갖고 비밀 키 생성
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //  인증 객체를 이용하여 토큰을 생성
    public Tokens generateTokens(Authentication authentication) {

        // 권한들을 가져와서 문자열로 변
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": now + 15m
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return Tokens.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .build();
    }

    // accessToken에서 username과 authority 정보를 추출하여 Authentication 객체를 생성하고 리턴
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 클레임에서 권한 정보가 없으면 유효하지 않은 토큰
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 유효성 체크
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", "JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return false;
    }

    // 토큰 복호화하여 Claim 객체 만들기
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
