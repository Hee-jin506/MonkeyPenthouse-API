package com.monkeypenthouse.core.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketOfAmenityVo {

    private Long id;
    private String title;
    private String description;
    private int maxCount;
    private int price;
    private Integer availableCount;
}
