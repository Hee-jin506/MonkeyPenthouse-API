package com.monkeypenthouse.core.controller.dto.purchase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseCreateResI {
    private final Integer amount;
    private final String orderId;
    private final String orderName;
}
