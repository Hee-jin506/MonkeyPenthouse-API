package com.monkeypenthouse.core.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class GetByIdResponseVo {
    private Long id;
    private String title;
    private String detail;
    private String address;
    private LocalDate startDate;
    private LocalDate deadlineDate;
    private List<String> bannerImages;
    private List<String> detailImages;
    private List<String> categories;
    private int recommended;
    private int minPersonNum;
    private int maxPersonNum;
    private int currentPersonNum;
    private int status;
    private int fundingPrice;
    private Long dibs;
}
