package com.monkeypenthouse.core.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refresh-token", timeToLive = 1209600)
@Data
public class RefreshToken {
    @Id
    private String key;
    private String value;

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
