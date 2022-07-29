package com.monkeypenthouse.core.controller.dto.photo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PhotoGetCarouselsResI {
    private final List<PhotoCarouselDataI> carousels;
}
