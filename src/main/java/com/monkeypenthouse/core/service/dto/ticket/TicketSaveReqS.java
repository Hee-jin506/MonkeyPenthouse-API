package com.monkeypenthouse.core.service.dto.ticket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TicketSaveReqS {
    private final String name;
    private final String detail;
    private final int capacity;
    private final int price;
    private final LocalDateTime eventDateTime;
}
