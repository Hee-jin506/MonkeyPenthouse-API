package com.monkeypenthouse.core.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckUserResponseVo {
    private boolean result;
    private String token;
}
