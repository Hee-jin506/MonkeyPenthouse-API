package com.monkeypenthouse.core.service.dto.amenity;

import com.monkeypenthouse.core.controller.dto.amenity.AmenitySimpleResI;
import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Builder
public class AmenitySimpleResS {
    private final Long id;
    private final String title;
    private final int minPersonNum;
    private final int maxPersonNum;
    private final int currentPersonNum;
    private final String thumbnailName;
    private final String address;
    private final LocalDate startDate;
    private final AmenityStatus status;

    public AmenitySimpleResI toI() {
        return new AmenitySimpleResI(
                id, title, minPersonNum, maxPersonNum, currentPersonNum, thumbnailName, address, startDate, status
        );
    }
}
