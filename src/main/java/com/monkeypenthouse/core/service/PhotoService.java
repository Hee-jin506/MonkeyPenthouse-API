package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.service.dto.photo.PhotoAddCarouselReqS;
import com.monkeypenthouse.core.service.dto.photo.PhotoGetCarouselsResS;
import org.jets3t.service.CloudFrontServiceException;

import java.io.IOException;

public interface PhotoService {
    void addCarousels(final PhotoAddCarouselReqS params) throws IOException;
    PhotoGetCarouselsResS getCarousels() throws CloudFrontServiceException, IOException;

    void syncCarouselToRedis();
}
