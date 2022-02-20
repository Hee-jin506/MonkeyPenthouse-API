package com.monkeypenthouse.core.exception;

import com.monkeypenthouse.core.dao.LoginType;
import com.monkeypenthouse.core.dao.User;
import com.monkeypenthouse.core.dto.UserDTO.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class SocialLoginFailedException extends AuthFailedException {

    private final LoginType loginType;

    public SocialLoginFailedException(LoginType loginType) {
        super("소셜 로그인(" + loginType.name() + ")에 실패하였습니다. (유효하지 않은 인증 정보)");
        this.loginType = loginType;
        super.user = null;
    }

    public SocialLoginFailedException(LoginType loginType, User user) {
        super(4000, "추가 정보 입력이 필요합니다.");
        super.user = user;
        this.loginType = loginType;
    }

    @Override
    public Optional<SignupReqDTO> getUserDTO(ModelMapper modelMapper) {
        if (user != null) {
            return Optional.of(modelMapper.map(user, SignupReqDTO.class));
        } else{
            return Optional.empty();
        }
    }

    public LoginType getLoginType() {
        return loginType;
    }
}
