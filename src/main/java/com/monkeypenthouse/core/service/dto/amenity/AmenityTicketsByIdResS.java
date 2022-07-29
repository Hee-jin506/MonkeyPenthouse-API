package com.monkeypenthouse.core.service.dto.amenity;

import com.monkeypenthouse.core.controller.dto.amenity.AmenityTicketsByIdResI;
import com.monkeypenthouse.core.controller.dto.ticket.TicketOfAmenityI;
import com.monkeypenthouse.core.service.dto.ticket.TicketOfAmenityS;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder
public class AmenityTicketsByIdResS {
    private final List<TicketOfAmenityS> tickets;

    public AmenityTicketsByIdResI toI() {
        return new AmenityTicketsByIdResI(
                tickets.stream().map(TicketOfAmenityS::toI).collect(Collectors.toList())
        );
    }
}
