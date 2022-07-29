package com.monkeypenthouse.core.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TicketOfOrderedDto {

    private Long id;
    private String name;
    private String detail;
    private LocalDateTime eventDateTime;
    private int price;
    private int amount;

    @QueryProjection
    public TicketOfOrderedDto(Long id, String name, String detail, LocalDateTime eventDateTime, int price, int amount) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.eventDateTime = eventDateTime;
        this.price = price;
        this.amount = amount;
    }
}
