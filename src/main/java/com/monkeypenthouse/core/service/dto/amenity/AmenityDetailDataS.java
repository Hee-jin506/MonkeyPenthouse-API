package com.monkeypenthouse.core.service.dto.amenity;

import com.monkeypenthouse.core.controller.dto.amenity.AmenityDetailResI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AmenityDetailDataS {
    private final Long id;
    private final String title;
    private final String address;
    private final List<String> categories;
    private final List<String> bannerImages;
    private final List<String> detailImages;
    private final LocalDate deadlineDate;
    private final LocalDate startDate;
    private final String detail;
    private final int recommended;
    private final int minPersonNum;
    private final int maxPersonNum;
    private final int currentPersonNum;
    private final int status;
    private final int fundingPrice;
    private final int dibs;

    public AmenityDetailResI toI() {
        return new AmenityDetailResI(
                id,
                title,
                address,
                categories,
                bannerImages,
                detailImages,
                deadlineDate,
                startDate,
                detail,
                recommended,
                minPersonNum,
                maxPersonNum,
                currentPersonNum,
                status,
                fundingPrice,
                dibs
        );
    }
}
