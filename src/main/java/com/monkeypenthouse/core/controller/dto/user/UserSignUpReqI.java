package com.monkeypenthouse.core.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monkeypenthouse.core.repository.entity.LoginType;
import com.monkeypenthouse.core.service.dto.user.UserSignUpReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserSignUpReqI {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣|A-Za-z|1-9]{1,10}$")
    private String name;

    @JsonFormat(pattern = "yyyy.MM.dd")
    @NotNull(message = "생일은 필수 입력값입니다.")
    private LocalDate birth;

    // 0: 여성, 1: 남성
    @Max(value = 1)
    @Min(value = 0)
    @NotNull(message = "성별은 필수 입력값입니다.")
    private int gender;

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[$@!%*#?&A-Za-z])[A-Za-z0-9$@$!%*#?&]{8,16}$")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{9,11}$")
    private String phoneNum;

    // 1: 동의 0: 비동의
    @Max(value = 1)
    @Min(value = 0)
    @NotNull(message = "마케팅 정보 수신 동의 여부는 필수 입력값입니다.")
    private int infoReceivable;

    @NotNull(message = "로그인 타입은 필수 입력값입니다.")
    private LoginType loginType;

    public UserSignUpReqS toS() {
        return new UserSignUpReqS(
                name, birth, gender, email, password, phoneNum, infoReceivable, loginType
        );
    }
}
