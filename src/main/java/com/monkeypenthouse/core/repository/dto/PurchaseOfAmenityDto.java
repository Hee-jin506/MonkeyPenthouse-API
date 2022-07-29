package com.monkeypenthouse.core.repository.dto;

import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import com.monkeypenthouse.core.repository.entity.Purchase;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PurchaseOfAmenityDto {

    private Long id;
    private LocalDateTime createdAt;
    private List<TicketOfOrderedDto> tickets;
    private int totalPrice;

    @QueryProjection
    public PurchaseOfAmenityDto(Long id,
                            LocalDateTime createdAt,
                            List<TicketOfOrderedDto> tickets,
                            int totalPrice) {
        this.id = id;
        this.createdAt = createdAt;
        this.tickets = tickets;
        this.totalPrice = totalPrice;
    }

    public PurchaseOfAmenityDto(Purchase purchase, List<TicketOfOrderedDto> tickets) {
        this.id = purchase.getId();
        this.createdAt = purchase.getCreatedAt();
        this.tickets = tickets;
        this.totalPrice = purchase.getAmount();
    }
}
