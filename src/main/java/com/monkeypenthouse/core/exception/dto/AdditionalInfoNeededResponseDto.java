package com.monkeypenthouse.core.exception.dto;

import com.monkeypenthouse.core.repository.entity.LoginType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AdditionalInfoNeededResponseDto {
    private String name;
    private LocalDate birth;
    private int gender;
    private String email;
    private String password;
    private String phoneNum;
    private int infoReceivable;
    private LoginType loginType;
}
