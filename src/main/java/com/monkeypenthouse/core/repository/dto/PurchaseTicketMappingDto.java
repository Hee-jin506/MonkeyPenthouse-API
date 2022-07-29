package com.monkeypenthouse.core.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseTicketMappingDto {
    public final Long amenityId;
    public final Long ticketId;
    public final Integer quantity;
}
