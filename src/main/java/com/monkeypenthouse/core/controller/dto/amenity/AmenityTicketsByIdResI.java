package com.monkeypenthouse.core.controller.dto.amenity;

import com.monkeypenthouse.core.controller.dto.ticket.TicketOfAmenityI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AmenityTicketsByIdResI {
    private final List<TicketOfAmenityI> tickets;
}
