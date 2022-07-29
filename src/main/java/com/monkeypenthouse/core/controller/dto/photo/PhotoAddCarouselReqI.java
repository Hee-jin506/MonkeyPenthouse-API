package com.monkeypenthouse.core.controller.dto.photo;

import com.monkeypenthouse.core.service.dto.photo.PhotoAddCarouselReqS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class PhotoAddCarouselReqI {
    private List<PhotoCarouselFileI> carousels;

    public PhotoAddCarouselReqS toS() {
        return new PhotoAddCarouselReqS(
                carousels.stream().map(PhotoCarouselFileI::toS).collect(Collectors.toList())
        );
    }
}
