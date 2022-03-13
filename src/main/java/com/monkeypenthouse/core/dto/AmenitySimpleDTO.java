package com.monkeypenthouse.core.dto;

import com.monkeypenthouse.core.vo.AmenitySimpleVo;
import com.monkeypenthouse.core.vo.TicketOfAmenityVo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmenitySimpleDTO {
    private Long id;
    private String title;
    private int minPersonNum;
    private int maxPersonNum;
    private int currentPersonNum;
    private String thumbnailName;
    private String address;
    private LocalDate startDate;
    private int status;

    public static AmenitySimpleDTO of(AmenitySimpleVo vo) {
        return builder()
                .id(vo.getId())
                .title(vo.getTitle())
                .minPersonNum(vo.getMinPersonNum())
                .maxPersonNum(vo.getMaxPersonNum())
                .currentPersonNum(vo.getCurrentPersonNum())
                .thumbnailName(vo.getThumbnailName())
                .address(vo.getAddress())
                .startDate(vo.getStartDate())
                .status(vo.getStatus())
                .build();
    }

}
