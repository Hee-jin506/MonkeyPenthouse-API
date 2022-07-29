package com.monkeypenthouse.core.controller.dto.purchase;

import com.monkeypenthouse.core.service.dto.purchase.PurchaseApproveReqS;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchaseApproveReqI {
    private String paymentKey;
    private String orderId;
    private Integer amount;

    public PurchaseApproveReqS toS() {
        return new PurchaseApproveReqS(
                paymentKey, orderId, amount
        );
    }
}
