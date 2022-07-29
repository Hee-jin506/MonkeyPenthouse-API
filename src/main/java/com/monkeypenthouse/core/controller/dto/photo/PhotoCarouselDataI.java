package com.monkeypenthouse.core.controller.dto.photo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PhotoCarouselDataI {
    private final String url;
    private final long amenityId;
}
