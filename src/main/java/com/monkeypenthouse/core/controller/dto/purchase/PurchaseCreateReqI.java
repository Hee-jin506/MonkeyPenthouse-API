package com.monkeypenthouse.core.controller.dto.purchase;

import com.monkeypenthouse.core.service.dto.purchase.PurchaseCreateReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class PurchaseCreateReqI {
    private List<PurchaseTicketMappingI> purchaseTicketMappingDtoList;

    public PurchaseCreateReqS toS() {
        return new PurchaseCreateReqS(
                purchaseTicketMappingDtoList.stream().map(PurchaseTicketMappingI::toS).collect(Collectors.toList())
        );
    }
}
