package com.monkeypenthouse.core.service.dto.purchase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseApproveReqS {
    private final String paymentKey;
    private final String orderId;
    private final Integer amount;
}
