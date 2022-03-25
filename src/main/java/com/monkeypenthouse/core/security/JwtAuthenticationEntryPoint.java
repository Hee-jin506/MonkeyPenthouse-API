package com.monkeypenthouse.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.*;
import com.monkeypenthouse.core.constant.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 유저 정보 없이 접근하면 SC_UNAUTHORIZED(401) 응답
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final CommonResponseMaker commonResponseMaker;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String servletPath = request.getServletPath();
        CommonResponseEntity commonResponseEntity = servletPath.equals("/user/reissue") ?
                commonResponseMaker.makeCommonResponse(ResponseCode.REISSUE_FAILED)
                : commonResponseMaker.makeCommonResponse(ResponseCode.AUTHENTICATION_FAILED);
        response.setStatus(commonResponseEntity.getStatusCode().value());
        String json = new ObjectMapper().writeValueAsString(commonResponseEntity.getBody());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(json);
        response.flushBuffer();
    }


}
