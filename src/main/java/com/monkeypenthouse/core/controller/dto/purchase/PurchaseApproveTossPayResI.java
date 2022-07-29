package com.monkeypenthouse.core.controller.dto.purchase;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseApproveTossPayResI {

    public String mId;
    public String version;
    public String paymentKey;
    public String orderId;
    public String orderName;
    public String currency;
    public String method;
    public Integer totalAmount;
    public Integer balanceAmount;
    public Integer suppliedAmount;
    public Integer vat;
    public String status;
    public String requestedAt;
    public String approvedAt;
    public Boolean useEscrow;
    public Boolean cultureExpense;
    public String virtualAccount;
    public String transfer;
    public String mobilePhone;
    public String giftCertificate;
    public String cashReceipt;
    public String discount;
    public String cancels;
    public String secret;
    public String type;
    public String easyPay;
    public Integer taxFreeAmount;
    public Card card;

    @Data
    public class Card {
        public String company;
        public String number;
        public Integer installmentPlanMonths;
        public Boolean isInterestFree;
        public String approveNo;
        public Boolean useCardPoint;
        public String cardType;
        public String ownerType;
        public String acquireStatus;
        public String receiptUrl;
    }

}