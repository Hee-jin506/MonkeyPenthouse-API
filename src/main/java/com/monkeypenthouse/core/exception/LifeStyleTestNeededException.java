package com.monkeypenthouse.core.exception;

import com.monkeypenthouse.core.entity.User;
import com.monkeypenthouse.core.dto.UserDTO.*;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class LifeStyleTestNeededException extends AuthFailedException {

    public LifeStyleTestNeededException(User user) {
        super(4001, "라이프스타일 테스트 미완료 회원입니다.");
        super.user = user;
    }

    @Override
    public Optional<SignupResDTO> getUserDTO(ModelMapper modelMapper) {
        if (user != null) {
            return Optional.of(modelMapper.map(user, SignupResDTO.class));
        } else{
            return Optional.empty();
        }
    }

}
