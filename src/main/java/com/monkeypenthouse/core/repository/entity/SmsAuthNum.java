package com.monkeypenthouse.core.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "sms-auth-num", timeToLive = 150)
@Data
public class SmsAuthNum implements Serializable {

    @Id
    private String userPhoneNum;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdDateTime;

    private String authNum;

    public SmsAuthNum(String userPhoneNum, String authNum) {
        this.userPhoneNum = userPhoneNum;
        this.authNum = authNum;
        this.createdDateTime = LocalDateTime.now();
    }

}
