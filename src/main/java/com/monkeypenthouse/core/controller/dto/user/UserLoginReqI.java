package com.monkeypenthouse.core.controller.dto.user;

import com.monkeypenthouse.core.service.dto.user.UserLoginReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class UserLoginReqI {
    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[$@!%*#?&A-Za-z])[A-Za-z0-9$@$!%*#?&]{8,16}$")
    private final String password;

    public UserLoginReqS toS() {
        return new UserLoginReqS(email, password);
    }
}
