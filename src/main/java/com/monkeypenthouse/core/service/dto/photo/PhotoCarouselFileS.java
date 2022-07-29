package com.monkeypenthouse.core.service.dto.photo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class PhotoCarouselFileS {
    private final MultipartFile file;
    private final long amenityId;
}
