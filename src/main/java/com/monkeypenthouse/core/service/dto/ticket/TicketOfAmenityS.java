package com.monkeypenthouse.core.service.dto.ticket;

import com.monkeypenthouse.core.controller.dto.ticket.TicketOfAmenityI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class TicketOfAmenityS {
    private final Long id;
    private final String title;
    private final String description;
    private final int maxCount;
    private final int price;
    private final Integer availableCount;

    public TicketOfAmenityI toI() {
        return new TicketOfAmenityI(
                id, title, description, maxCount, price, availableCount
        );
    }
}
