package com.monkeypenthouse.core.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class CurrentPersonAndFundingPriceAndDibsOfAmenityDTO {
    private int currentPersonNum;
    private int fundingPrice;
    private Long dibs;

    @QueryProjection
    public CurrentPersonAndFundingPriceAndDibsOfAmenityDTO(int currentPersonNum,
                                                           int fundingPrice,
                                                           Long dibs) {
        this.currentPersonNum = currentPersonNum;
        this.fundingPrice = fundingPrice;
        this.dibs = dibs;
    }
}
