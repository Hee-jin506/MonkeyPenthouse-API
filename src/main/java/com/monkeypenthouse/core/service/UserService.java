package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.repository.entity.LoginType;
import com.monkeypenthouse.core.repository.entity.User;
import com.monkeypenthouse.core.service.dto.user.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {


    // 회원 추가
    UserSignUpResS add(final UserSignUpReqS params) throws DataIntegrityViolationException;

    // id에 따른 조회
    User getById(final Long id);

    // 이메일에 따른 조회
    User getUserByEmail(final String email);

    // 이메일에 따른 조회
    User getUserByEmailAndLoginType(final String email, final  LoginType loginType);

    // 특정 이메일의 회원이 존해자는지 확인
    boolean checkEmailDuplicate(final String email);

    // 특정 전화번호를 가진 회원이 존해자는지 확인
    void checkPhoneNumDuplicate(final String phoneNum);

    // 로그인
    UserLoginResS login(final UserLoginReqS params) throws AuthenticationException;

    // accessToken 재발급
    UserGetTokenResS reissue(final String refreshToken);

    // 현재 SecurityContext에 있는 유저 정보 가져오기
    UserGetMeResS getMyInfo();

    // 카카오톡 인증 후 회원 정보 조회
    // 유저 정보가 없을 시 비어있는 유저 리턴
    UserLoginResS authKakao(final String token);

    // 네이버 인증 후 회원 정보 조회
    UserLoginResS authNaver(final String token);

    // 유저의 이메일 찾기
    UserGetEmailByPhoneNumResS findEmail(final String phoneNum);

    // 유저의 비밀번호 수정
    void updatePassword(final UserDetails userDetails, final String password);

    // 유저의 라이프스타일 수정
    void updateLifeStyle(final UserUpdateLifeStyleReqS params);

    // 특정 이메일을 가진 회원 삭제
    void deleteByEmail(final String email);

    // 로그아웃
    void logout() throws Exception;

    // 이메일과 전화번호로 회원여부를 확인
    UserCheckExistResS checkUser(final UserCheckExistReqS params);
}
