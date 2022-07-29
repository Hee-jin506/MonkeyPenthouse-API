package com.monkeypenthouse.core.service.dto.photo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PhotoAddCarouselReqS {
    private final List<PhotoCarouselFileS> carousels;
}
