package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.service.dto.amenity.*;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

public interface AmenityService {
    void add(final AmenitySaveReqS params) throws Exception;
    AmenityGetByIdResS getById(final Long id) throws CloudFrontServiceException, IOException;

    AmenityGetPagesResS getAmenitiesDibsOn(final UserDetails userDetails, final Pageable pageable) throws CloudFrontServiceException, IOException;

    AmenityGetPagesResS getPageByRecommended(final Pageable pageable) throws CloudFrontServiceException, IOException;

    AmenityGetPagesResS getPage(final Pageable pageable) throws CloudFrontServiceException, IOException;

    AmenityGetPagesResS getPageByCategory(final Long category, final Pageable pageable) throws CloudFrontServiceException, IOException;

    AmenityTicketsByIdResS getTicketsOfAmenity(final Long amenityId);

    void updateStatusOfAmenity();

    AmenityGetViewedResS getViewed(final List<Long> amenityIds) throws CloudFrontServiceException, IOException;

    AmenityGetPagesResS getAmenitiesByOrdered(final UserDetails userDetails, final Pageable pageable) throws CloudFrontServiceException, IOException;

    AmenityPurchasesOfOrderedResS getPurchasesOfOrderedAmenity(final UserDetails userDetails, final Long amenityId) throws CloudFrontServiceException, IOException;

    void handleClosingProcessOfAmenity();
}
