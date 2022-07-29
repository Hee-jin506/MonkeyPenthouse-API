package com.monkeypenthouse.core.controller.dto.ticket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketOfAmenityI {
    private final Long id;
    private final String title;
    private final String description;
    private final int maxCount;
    private final int price;
    private final Integer availableCount;
}
