package com.monkeypenthouse.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LifeStyleNeededResponseDto {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate birth;
    private int gender;
    private String email;
    private String phoneNum;
    private String roomId;
}
