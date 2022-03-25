package com.monkeypenthouse.core.dto.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monkeypenthouse.core.dto.UserDTO;
import com.monkeypenthouse.core.entity.LoginType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Builder
public class AdditionalInfoNeededResponseDTO {
    private String name;
    private LocalDate birth;
    private int gender;
    private String email;
    private String password;
    private String phoneNum;
    private int infoReceivable;
    private LoginType loginType;
}
