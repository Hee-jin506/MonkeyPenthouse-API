package com.monkeypenthouse.core.service.dto.amenity;

import com.monkeypenthouse.core.controller.dto.amenity.AmenityGetViewedResI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder
public class AmenityGetViewedResS {
    private final List<AmenitySimpleResS> amenities;
    public AmenityGetViewedResI toI() {
        return new AmenityGetViewedResI(
                amenities.stream().map(AmenitySimpleResS::toI).collect(Collectors.toList())
        );
    }
}
