package com.monkeypenthouse.core.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AmenitySimpleVo {
    private Long id;
    private String title;
    private int minPersonNum;
    private int maxPersonNum;
    private int currentPersonNum;
    private String thumbnailName;
    private String address;
    private LocalDate startDate;
    private int status;
}
