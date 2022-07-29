package com.monkeypenthouse.core.service.dto.purchase;

import com.monkeypenthouse.core.controller.dto.purchase.PurchaseCreateResI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class PurchaseCreateResS {
    private final Integer amount;
    private final String orderId;
    private final String orderName;

    public PurchaseCreateResI toI() {
        return new PurchaseCreateResI(amount, orderId, orderName);
    }
}
