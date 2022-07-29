package com.monkeypenthouse.core.service.dto.amenity;

import com.monkeypenthouse.core.controller.dto.amenity.AmenityTicketsOfOrderedResI;
import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import com.monkeypenthouse.core.service.dto.purchase.PurchaseByAmenityAndUserResS;
import com.monkeypenthouse.core.service.dto.ticket.TicketOfOrderedS;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder
public class AmenityPurchasesOfOrderedResS {

    private final List<String> bannerImages;
    private final String title;
    private final int maxPersonNum;
    private final int currentPersonNum;
    private final AmenityStatus status;
    private final List<PurchaseByAmenityAndUserResS> purchases;

    public AmenityTicketsOfOrderedResI toI() {
        return new AmenityTicketsOfOrderedResI(
                bannerImages, title, maxPersonNum, currentPersonNum, status, purchases.stream().map(PurchaseByAmenityAndUserResS::toI).collect(Collectors.toList())
        );
    }
}
