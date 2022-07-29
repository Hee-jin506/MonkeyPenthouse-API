package com.monkeypenthouse.core.service.dto.user;

import com.monkeypenthouse.core.repository.entity.LifeStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdateLifeStyleReqS {
    private final Long id;
    private final LifeStyle lifeStyle;
}
