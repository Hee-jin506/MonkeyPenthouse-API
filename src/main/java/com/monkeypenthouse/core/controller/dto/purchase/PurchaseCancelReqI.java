package com.monkeypenthouse.core.controller.dto.purchase;

import com.monkeypenthouse.core.service.dto.purchase.PurchaseCancelReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public class PurchaseCancelReqI {
    private String orderId;

    public PurchaseCancelReqS toS() {
        return new PurchaseCancelReqS(orderId);
    }
}
