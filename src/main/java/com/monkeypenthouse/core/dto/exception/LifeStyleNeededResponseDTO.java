package com.monkeypenthouse.core.dto.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LifeStyleNeededResponseDTO {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate birth;
    private int gender;
    private String email;
    private String phoneNum;
    private String roomId;
}
