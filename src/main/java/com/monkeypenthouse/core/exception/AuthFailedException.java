package com.monkeypenthouse.core.exception;

import com.monkeypenthouse.core.entity.User;
import com.monkeypenthouse.core.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class AuthFailedException extends ExpectedException {

    protected User user;

    public AuthFailedException() {
        super(HttpStatus.UNAUTHORIZED, "회원 인증에 실패하였습니다.");
    }

    public AuthFailedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public AuthFailedException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }

    public AuthFailedException(int detailStatus, String message) {
        super(HttpStatus.UNAUTHORIZED, detailStatus, message);
    }

    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public Optional<? extends UserDTO> getUserDTO(ModelMapper modelMapper) {
       if (user != null) {
           return Optional.of(modelMapper.map(user, UserDTO.class));
       } else {
           return Optional.empty();
       }
    }
}
