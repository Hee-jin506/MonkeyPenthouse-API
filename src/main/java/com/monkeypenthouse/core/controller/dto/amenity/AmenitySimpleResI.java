package com.monkeypenthouse.core.controller.dto.amenity;

import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AmenitySimpleResI {
    private final Long id;
    private final String title;
    private final int minPersonNum;
    private final int maxPersonNum;
    private final int currentPersonNum;
    private final String thumbnailName;
    private final String address;
    private final LocalDate startDate;
    private final AmenityStatus status;
}
