package com.monkeypenthouse.core.service.dto.purchase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseByAmenityAndUserReqS {
    private final Long amenityId;
    private final Long userId;
}
