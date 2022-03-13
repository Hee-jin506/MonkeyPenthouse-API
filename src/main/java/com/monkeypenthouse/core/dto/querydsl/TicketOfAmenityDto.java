package com.monkeypenthouse.core.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TicketOfAmenityDto {

    private Long id;
    private String title;
    private String description;
    private int maxCount;
    private int price;
    private Integer reservedCount;

    @QueryProjection
    public TicketOfAmenityDto(Long id, String title, String description, int maxCount, int price, Integer reservedCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxCount = maxCount;
        this.price = price;
        this.reservedCount = reservedCount;
    }
}
