package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.dto.AmenitySimpleDto;
import com.monkeypenthouse.core.repository.dto.CurrentPersonAndFundingPriceAndDibsOfAmenityDto;
import com.monkeypenthouse.core.repository.dto.TicketOfAmenityDto;
import com.monkeypenthouse.core.repository.dto.TicketOfOrderedDto;
import com.monkeypenthouse.core.repository.entity.Amenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AmenityRepositoryCustom {

    Optional<CurrentPersonAndFundingPriceAndDibsOfAmenityDto> findcurrentPersonAndFundingPriceAndDibsOfAmenityById(Long id);

    Page<AmenitySimpleDto> findPageByRecommended(int recommended, Pageable pageable);

    Page<AmenitySimpleDto> findPage(Pageable pageable);

    Page<AmenitySimpleDto> findPageByCategory(Long categoryId, Pageable pageable);

    Page<AmenitySimpleDto> findPageByDibsOfUser(Long userId, Pageable pageable);

    List<TicketOfAmenityDto> getTicketsOfAmenity(Long amenityId);

    List<AmenitySimpleDto> findAllById(List<Long> amenityIds);

    Page<AmenitySimpleDto> findPageByOrdered(Long id, Pageable pageable);

    int countTotalQuantity(Long amenityId);

    int countPurchasedQuantity(Long amenityId);

    List<Amenity> findAllAmenitiesToBeClosed(LocalDate today);

    List<Amenity> findAllAmenitiesToBeEnded(LocalDate today);
}
