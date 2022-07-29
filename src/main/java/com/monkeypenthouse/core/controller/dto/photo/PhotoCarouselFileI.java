package com.monkeypenthouse.core.controller.dto.photo;

import com.monkeypenthouse.core.service.dto.photo.PhotoCarouselFileS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public class PhotoCarouselFileI {
    private MultipartFile file;
    private long amenityId;

    public PhotoCarouselFileS toS() {
        return new PhotoCarouselFileS(
                file, amenityId
        );
    }
}
