package com.monkeypenthouse.core.service.dto.ticket;

import com.monkeypenthouse.core.controller.dto.ticket.TicketOfOrderedI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class TicketOfOrderedS {
    private final Long id;
    private final LocalDateTime eventDateTime;
    private final String name;
    private final String detail;
    private final int price;
    private final int amount;

    public TicketOfOrderedI toI() {
        return new TicketOfOrderedI(
                id, eventDateTime, name, detail, price, amount
        );
    }
}
