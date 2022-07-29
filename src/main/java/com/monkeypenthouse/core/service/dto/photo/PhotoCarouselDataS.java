package com.monkeypenthouse.core.service.dto.photo;

import com.monkeypenthouse.core.controller.dto.photo.PhotoCarouselDataI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class PhotoCarouselDataS {
    private final String url;
    private final long amenityId;

    public PhotoCarouselDataI toI() {
        return new PhotoCarouselDataI(url, amenityId);
    }
}
