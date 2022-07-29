package com.monkeypenthouse.core.controller.dto.purchase;

import lombok.Data;

@Data
public class PurchaseRefundTossPayResI {

    public Integer statusCode;
    public String mId;
    public String version;
    public String transactionKey;
    public String paymentKey;
    public String orderId;
    public String orderName;
    public String currency;
    public String method;
    public String status;
    public String requestedAt;
    public String approvedAt;
    public Boolean useEscrow;
    public Boolean cultureExpense;
    public String virtualAccount;
    public String transfer;
    public String mobilePhone;
    public String giftCertificate;
    public String foreignEasyPay;
    public String cashReceipt;
    public String discount;
    public String secret;
    public String type;
    public String easyPay;
    public String country;
    public String failure;
    public Integer totalAmount;
    public Integer balanceAmount;
    public Integer suppliedAmount;
    public Integer vat;
    public Integer taxFreeAmount;
    public Card card;
    public Cancels cancels;

    @Data
    public class Card {
        public String company;
        public String number;
        public Integer installmentPlanMonths;
        public Boolean isInterestFree;
        public String interestPayer;
        public String approveNo;
        public Boolean useCardPoint;
        public String cardType;
        public String ownerType;
        public String acquireStatus;
        public String receiptUrl;
    }

    @Data
    public class Cancels {
        public String cancelReason;
        public String canceledAt;
        public Integer cancelAmount;
        public Integer taxFreeAmount;
        public Integer taxAmount;
        public Integer refundableAmount;
    }

}