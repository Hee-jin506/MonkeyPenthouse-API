package com.monkeypenthouse.core.controller.dto.purchase;

import com.monkeypenthouse.core.service.dto.purchase.PurchaseTicketMappingS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseTicketMappingI {
    private Long amenityId;
    private Long ticketId;
    private Integer quantity;

    public PurchaseTicketMappingS toS() {
        return new PurchaseTicketMappingS(
                amenityId, ticketId, quantity
        );
    }
}
