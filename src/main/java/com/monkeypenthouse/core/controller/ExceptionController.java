package com.monkeypenthouse.core.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.monkeypenthouse.core.common.DefaultRes;
import com.monkeypenthouse.core.common.SocialLoginRes;
import com.monkeypenthouse.core.dto.UserDTO;
import com.monkeypenthouse.core.exception.AuthFailedException;
import com.monkeypenthouse.core.exception.DataNotFoundException;
import com.monkeypenthouse.core.exception.ExpectedException;
import com.monkeypenthouse.core.exception.SocialLoginFailedException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final ModelMapper modelMapper;

    // 중복된 데이터 추가 요청 시
    @ExceptionHandler({ DataIntegrityViolationException.class })
    protected ResponseEntity<DefaultRes<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.CONFLICT.value(),
                        e.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    // 존재하지 않는 리소스 요청 시
    @ExceptionHandler({ DataNotFoundException.class })
    protected ResponseEntity<DefaultRes<?>> handleDataNotFoundException(DataNotFoundException e) {
        return new ResponseEntity<>(
                DefaultRes.res(
                        e.getHttpStatus().value(),
                        e.getMessage()),
                e.getHttpStatus()
        );
    }

    // 로컬 로그인 실패 시 (이메일, 비번 오류)
    @ExceptionHandler({ AuthenticationException.class })
    protected ResponseEntity<DefaultRes<?>> handleAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.UNAUTHORIZED.value(),
                        e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 소셜 로그인 실패 시
    @ExceptionHandler({ SocialLoginFailedException.class })
    protected ResponseEntity<SocialLoginRes<?>> handleSocialLoginFailedException(SocialLoginFailedException e) {
        if (e.getUserDTO(modelMapper).isPresent()) {
            return new ResponseEntity<>(
                    SocialLoginRes.res(
                            e.getDetailStatus(),
                            e.getMessage(),
                            e.getUserDTO(modelMapper),
                            false),
                    e.getHttpStatus()
                    );
        } else {
            return new ResponseEntity<>(
                    SocialLoginRes.res(
                            e.getDetailStatus(),
                            e.getMessage(),
                            false),
                    e.getHttpStatus()
            );
        }
    }

    // 회원 인증 실패 시
    @ExceptionHandler({ AuthFailedException.class })
    protected ResponseEntity<DefaultRes<?>> handleAuthFailedException(AuthFailedException e) {
        if (e.getUserDTO(modelMapper).isPresent()) {
            return new ResponseEntity<>(
                    DefaultRes.res(
                            e.getDetailStatus(),
                            e.getMessage(),
                            e.getUserDTO(modelMapper)),
                    e.getHttpStatus()
            );
        } else {
            return new ResponseEntity<>(
                    SocialLoginRes.res(
                            e.getDetailStatus(),
                            e.getMessage()),
                    e.getHttpStatus()
            );
        }
    }

    // json 파싱 실패시
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    protected ResponseEntity<DefaultRes<?>> handleHttpMessageNotReadableException(JsonMappingException e) {
        List<String> fieldErrors = new ArrayList<>();
        for (JsonMappingException.Reference fieldError : e.getPath()) {
            fieldErrors.add(fieldError.getFieldName());
        }
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.BAD_REQUEST.value(),
                        "정보가 유효하지 않습니다. (json 파싱 오류)",
                        fieldErrors),
                HttpStatus.BAD_REQUEST
        );
    }

    // @valid 유효성 검사 실패시
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    protected ResponseEntity<DefaultRes<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> fieldErrors = new ArrayList<>();
            BindingResult bindingResult = e.getBindingResult();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrors.add(fieldError.getField());
            }

            return new ResponseEntity<>(
                    DefaultRes.res(
                            HttpStatus.BAD_REQUEST.value(),
                            "정보가 유효하지 않습니다. (형식을 벗어난 값)",
                            fieldErrors),
                    HttpStatus.BAD_REQUEST
            );

    }

    // 예상가능한 Exception 처리
    @ExceptionHandler(ExpectedException.class)
    public ResponseEntity<DefaultRes<?>> handleExpectedException(ExpectedException e) {
        return new ResponseEntity<>(
                DefaultRes.res(
                        e.getHttpStatus().value(),
                        e.getMessage()),
                e.getHttpStatus()
        );
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultRes<?>> handleAll(final Exception e) {
        System.out.println("e = " + e);
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "서버의 내부적인 오류로 인해 문제가 발생하였습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
