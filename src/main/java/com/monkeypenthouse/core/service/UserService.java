package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.entity.LoginType;
import com.monkeypenthouse.core.entity.Tokens;
import com.monkeypenthouse.core.entity.User;
import com.monkeypenthouse.core.exception.AuthFailedException;
import com.monkeypenthouse.core.exception.DataNotFoundException;
import com.monkeypenthouse.core.exception.ExpectedException;
import com.monkeypenthouse.core.vo.CheckUserResponseVo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface UserService {


    // 회원 추가
    User add(User user) throws DataIntegrityViolationException, DataNotFoundException;

    // id에 따른 조회
    User getById(Long id) throws DataNotFoundException;

    // 이메일에 따른 조회
    User getUserByEmail(String email) throws DataNotFoundException;

    // 이메일에 따른 조회
    User getUserByEmailAndLoginType(String email, LoginType loginType) throws DataNotFoundException;

    // 특정 이메일의 회원이 존해자는지 확인
    boolean checkEmailDuplicate(String email);

    // 특정 전화번호를 가진 회원이 존해자는지 확인
    void checkPhoneNumDuplicate(String phoneNum) throws ExpectedException;

    // 로그인
    Map<String, Object> login(User user) throws AuthenticationException, AuthFailedException;

    // accessToken 재발급
    Tokens reissue(String refreshToken) throws AuthFailedException;

    // 현재 SecurityContext에 있는 유저 정보 가져오기
    User getMyInfo() throws AuthFailedException;

    // 카카오톡 인증 후 회원 정보 조회
    // 유저 정보가 없을 시 비어있는 유저 리턴
    User authKakao(String token) throws AuthFailedException;

    // 네이버 인증 후 회원 정보 조회
    User authNaver(String token) throws AuthFailedException;

    // 유저의 이메일 찾기
    User findEmail(String phoneNum) throws AuthFailedException;

    // 유저의 비밀번호 수정
    void updatePassword(UserDetails userDetails, String password) throws AuthFailedException;

    // 유저의 라이프스타일 수정
    void updateLifeStyle(User user) throws AuthFailedException;

    // 특정 이메일을 가진 회원 삭제
    void deleteByEmail(String email)  throws DataNotFoundException;

    // 로그아웃
    void logout() throws Exception;

    // 이메일과 전화번호로 회원여부를 확인
    CheckUserResponseVo checkUser(String phoneNum, String email);
}
