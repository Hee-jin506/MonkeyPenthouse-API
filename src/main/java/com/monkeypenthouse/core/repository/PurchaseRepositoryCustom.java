package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.dto.PurchaseOfAmenityDto;
import com.monkeypenthouse.core.repository.dto.TicketOfOrderedDto;

import java.util.List;

public interface PurchaseRepositoryCustom {
    List<PurchaseOfAmenityDto> findAllByAmenityIdAndUserId(Long userId, Long amenityId);
}
