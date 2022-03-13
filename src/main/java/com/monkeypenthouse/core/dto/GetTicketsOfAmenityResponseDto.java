package com.monkeypenthouse.core.dto;

import com.monkeypenthouse.core.vo.GetTicketsOfAmenityResponseVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketsOfAmenityResponseDto {

    private List<TicketOfAmenityDto> tickets;

    public static GetTicketsOfAmenityResponseDto of(GetTicketsOfAmenityResponseVo vo) {
        return builder()
                .tickets(
                        vo.getTickets()
                                .stream()
                                .map(TicketOfAmenityVo -> TicketOfAmenityDto.of(TicketOfAmenityVo))
                                .collect(Collectors.toList()))
                .build();
    }
}
