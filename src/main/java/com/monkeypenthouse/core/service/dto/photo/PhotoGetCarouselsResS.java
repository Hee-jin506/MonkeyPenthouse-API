package com.monkeypenthouse.core.service.dto.photo;

import com.monkeypenthouse.core.controller.dto.photo.PhotoGetCarouselsResI;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder
public class PhotoGetCarouselsResS {
    private final List<PhotoCarouselDataS> carousels;

    public PhotoGetCarouselsResI toI() {
        return new PhotoGetCarouselsResI(carousels.stream().map(PhotoCarouselDataS::toI).collect(Collectors.toList()));
    }
}
