package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.controller.dto.purchase.PurchaseApproveReqI;
import com.monkeypenthouse.core.controller.dto.purchase.PurchaseCancelReqI;
import com.monkeypenthouse.core.controller.dto.purchase.PurchaseCreateReqI;
import com.monkeypenthouse.core.controller.dto.purchase.PurchaseRefundReqI;
import com.monkeypenthouse.core.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/purchase")
@Log4j2
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CommonResponseMaker commonResponseMaker;

    @PostMapping(value = "/create")
    public CommonResponseEntity createPurchase(@AuthenticationPrincipal final UserDetails userDetails,
                                               @RequestBody final PurchaseCreateReqI request) throws Exception {
        return commonResponseMaker.makeCommonResponse(
                purchaseService.createPurchase(userDetails, request.toS()).toI(), ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/approve")
    public CommonResponseEntity approvePurchase(@RequestBody final PurchaseApproveReqI request) throws IOException, InterruptedException {
        purchaseService.approvePurchase(request.toS());
        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/cancel")
    public CommonResponseEntity cancelPurchase(@RequestBody final PurchaseCancelReqI request) {
        purchaseService.cancelPurchase(request.toS());
        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/refund")
    public CommonResponseEntity refundPurchase(@RequestBody final PurchaseRefundReqI request) throws IOException, InterruptedException {
        purchaseService.refundPurchase(request.getPurchaseId());
        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }
}
