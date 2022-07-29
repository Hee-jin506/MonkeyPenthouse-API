package com.monkeypenthouse.core.controller.dto.purchase;

import com.monkeypenthouse.core.controller.dto.ticket.TicketOfOrderedI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PurchaseByAmenityAndUserResI {
    private final Long id;
    private final LocalDateTime createdAt;
    private final List<TicketOfOrderedI> tickets;
    private final int totalPrice;

}
