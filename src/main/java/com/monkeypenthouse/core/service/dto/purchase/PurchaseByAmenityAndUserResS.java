package com.monkeypenthouse.core.service.dto.purchase;

import com.monkeypenthouse.core.controller.dto.purchase.PurchaseByAmenityAndUserResI;
import com.monkeypenthouse.core.service.dto.ticket.TicketOfOrderedS;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder
public class PurchaseByAmenityAndUserResS {

    private final Long id;
    private final LocalDateTime createdAt;
    private final List<TicketOfOrderedS> tickets;
    private final int totalPrice;

    public PurchaseByAmenityAndUserResI toI() {
        return new PurchaseByAmenityAndUserResI(id, createdAt, tickets.stream().map(TicketOfOrderedS::toI).collect(Collectors.toList()), totalPrice);
    }
}
