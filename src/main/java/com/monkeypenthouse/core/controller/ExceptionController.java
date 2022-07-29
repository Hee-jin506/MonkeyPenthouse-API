package com.monkeypenthouse.core.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseBody;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final CommonResponseMaker commonResponseMaker;

    // CommonException 통합
    @ExceptionHandler({CommonException.class})
    protected CommonResponseEntity handleCommonException(final CommonException e) {

        return e.getData() == null ?
                commonResponseMaker.makeCommonResponse(e.getCode())
                : commonResponseMaker.makeCommonResponse(e.getData(), e.getCode());
    }

    // 중복된 데이터 추가 요청 시
    @ExceptionHandler({DataIntegrityViolationException.class})
    protected CommonResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException e) {

        return commonResponseMaker.makeCommonResponse(ResponseCode.DATA_INTEGRITY_VIOLATED);
    }

    // 로컬 로그인 실패 시 (이메일, 비번 오류)
    @ExceptionHandler({AuthenticationException.class})
    protected CommonResponseEntity handleAuthenticationException(AuthenticationException e) {

        return commonResponseMaker.makeCommonResponse(ResponseCode.AUTHENTICATION_FAILED);
    }

    // multipart/form-data or json 파싱 실패시
    @ExceptionHandler({HttpMessageNotReadableException.class, BindException.class})
    protected CommonResponseEntity handleHttpMessageNotReadableException(Exception e) {

        return commonResponseMaker.makeCommonResponse(ResponseCode.HTTP_MESSAGE_NOT_READABLE);
    }

    // @valid 유효성 검사 실패시 (RequestBody)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected CommonResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return commonResponseMaker.makeCommonResponse(ResponseCode.METHOD_ARGUMENT_NOT_VALID);
    }

    // @valid 유효성 검사 실패시 (RequestParam, PathVariable)
    @ExceptionHandler({ConstraintViolationException.class})
    protected CommonResponseEntity handleMethodArgumentNotValidException(ConstraintViolationException e) {

        return commonResponseMaker.makeCommonResponse(ResponseCode.CONSTRAINT_VIOLATED);
    }

    // @valid 유효성 검사 실패시 (RequestParam, PathVariable)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    protected CommonResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        return commonResponseMaker.makeCommonResponse(ResponseCode.MISSING_PARAMETER);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public CommonResponseEntity handleAll(final Exception e) {
        e.printStackTrace();

        return commonResponseMaker.makeCommonResponse(ResponseCode.INTERNAL_SERVER_ERROR);
    }
}
