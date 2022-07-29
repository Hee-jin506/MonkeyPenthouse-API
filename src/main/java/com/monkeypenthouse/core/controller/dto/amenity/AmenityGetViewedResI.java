package com.monkeypenthouse.core.controller.dto.amenity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AmenityGetViewedResI {
    private final List<AmenitySimpleResI> amenities;
}
