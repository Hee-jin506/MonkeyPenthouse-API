package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.dto.querydsl.AmenitySimpleDTO;
import com.monkeypenthouse.core.dto.querydsl.CurrentPersonAndFundingPriceAndDibsOfAmenityDTO;
import com.monkeypenthouse.core.dto.querydsl.TicketOfAmenityDto;
import com.monkeypenthouse.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AmenityRepositoryCustom {

    Optional<CurrentPersonAndFundingPriceAndDibsOfAmenityDTO> findcurrentPersonAndFundingPriceAndDibsOfAmenityById(Long id);

    Page<AmenitySimpleDTO> findPageByRecommended(int recommended, Pageable pageable);

    Page<AmenitySimpleDTO> findPage(Pageable pageable);

    Page<AmenitySimpleDTO> findPageByCategory(Long categoryId, Pageable pageable);

    Page<AmenitySimpleDTO> findPageByDibsOfUser(Long userId, Pageable pageable);

    List<TicketOfAmenityDto> getTicketsOfAmenity(Long amenityId);
}
