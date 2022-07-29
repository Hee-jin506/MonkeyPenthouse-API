package com.monkeypenthouse.core.service.dto.purchase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PurchaseCreateReqS {
    private final List<PurchaseTicketMappingS> purchaseTicketMappingDtoList;
}
