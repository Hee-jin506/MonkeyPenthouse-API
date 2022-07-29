package com.monkeypenthouse.core.controller.dto.ticket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TicketOfOrderedI {
    private final Long id;
    private final LocalDateTime eventDateTime;
    private final String name;
    private final String detail;
    private final int price;
    private final int amount;
}
