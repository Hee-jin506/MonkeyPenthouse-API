package com.monkeypenthouse.core.repository.dto;

import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AmenitySimpleDto {
    private Long id;
    private String title;
    private int minPersonNum;
    private int maxPersonNum;
    private int currentPersonNum;
    private String thumbnailName;
    private String address;
    private LocalDate startDate;
    private AmenityStatus status;

    @QueryProjection
    public AmenitySimpleDto(Long id,
                            String title,
                            int minPersonNum,
                            int maxPersonNum,
                            int currentPersonNum,
                            String thumbnailName,
                            String address,
                            LocalDate startDate,
                            AmenityStatus status) {
        this.id = id;
        this.title = title;
        this.minPersonNum = minPersonNum;
        this.maxPersonNum = maxPersonNum;
        this.currentPersonNum = currentPersonNum;
        this.thumbnailName = thumbnailName;
        this.address = address;
        this.startDate = startDate;
        this.status = status;
    }
}
