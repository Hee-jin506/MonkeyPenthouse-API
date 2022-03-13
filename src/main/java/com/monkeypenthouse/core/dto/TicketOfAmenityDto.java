package com.monkeypenthouse.core.dto;

import com.monkeypenthouse.core.vo.TicketOfAmenityVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketOfAmenityDto {

    private Long id;
    private String title;
    private String description;
    private int maxCount;
    private int price;
    private Integer availableCount;

    public static TicketOfAmenityDto of(TicketOfAmenityVo vo) {
        return builder()
                .id(vo.getId())
                .title(vo.getTitle())
                .description(vo.getDescription())
                .maxCount(vo.getMaxCount())
                .price(vo.getPrice())
                .availableCount(vo.getAvailableCount())
                .build();
    }
}
