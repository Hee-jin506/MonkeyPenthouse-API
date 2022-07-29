package com.monkeypenthouse.core.service.dto.purchase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseCancelReqS {
    private final String orderId;
}
