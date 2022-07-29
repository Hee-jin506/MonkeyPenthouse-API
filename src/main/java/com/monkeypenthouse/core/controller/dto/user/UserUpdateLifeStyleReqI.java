package com.monkeypenthouse.core.controller.dto.user;

import com.monkeypenthouse.core.repository.entity.LifeStyle;
import com.monkeypenthouse.core.service.dto.user.UserUpdateLifeStyleReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class UserUpdateLifeStyleReqI {
    @NotNull(message = "id는 필수 입력값입니다.")
    private final Long id;
    @NotNull(message = "라이프 스타일은 필수 입력값입니다.")
    private final LifeStyle lifeStyle;

    public UserUpdateLifeStyleReqS toS() {
        return new UserUpdateLifeStyleReqS(id, lifeStyle);
    }
}
