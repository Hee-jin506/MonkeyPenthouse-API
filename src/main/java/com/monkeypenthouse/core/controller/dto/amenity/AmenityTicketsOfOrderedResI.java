package com.monkeypenthouse.core.controller.dto.amenity;

import com.monkeypenthouse.core.controller.dto.purchase.PurchaseByAmenityAndUserResI;
import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AmenityTicketsOfOrderedResI {
    private final List<String> bannerImages;
    private final String title;
    private final int maxPersonNum;
    private final int currentPersonNum;
    private final AmenityStatus status;
    private final List<PurchaseByAmenityAndUserResI> purchases;
}
