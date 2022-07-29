package com.monkeypenthouse.core.controller.dto.amenity;

import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AmenityGetByIdResI {
    private final Long id;
    private final String title;
    private final String detail;
    private final String address;
    private final LocalDate startDate;
    private final LocalDate deadlineDate;
    private final List<String> bannerImages;
    private final List<String> detailImages;
    private final List<String> categories;
    private final int recommended;
    private final int minPersonNum;
    private final int maxPersonNum;
    private final int currentPersonNum;
    private final AmenityStatus status;
    private final int fundingPrice;
    private final Long dibs;
}
