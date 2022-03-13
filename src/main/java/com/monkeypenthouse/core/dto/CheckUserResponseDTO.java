package com.monkeypenthouse.core.dto;

import com.monkeypenthouse.core.vo.CheckUserResponseVo;
import com.monkeypenthouse.core.vo.TicketOfAmenityVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserResponseDTO {
    private boolean result;
    private String token;

    public static CheckUserResponseDTO of(CheckUserResponseVo vo) {
        return builder()
                .result(vo.isResult())
                .token(vo.getToken())
                .build();
    }
}
