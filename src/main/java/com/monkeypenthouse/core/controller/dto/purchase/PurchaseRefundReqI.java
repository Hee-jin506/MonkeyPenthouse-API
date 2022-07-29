package com.monkeypenthouse.core.controller.dto.purchase;

import com.monkeypenthouse.core.service.dto.purchase.PurchaseRefundReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseRefundReqI {
    private Long purchaseId;
}
