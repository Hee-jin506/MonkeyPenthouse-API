package com.monkeypenthouse.core.exception;

import com.monkeypenthouse.core.constant.ResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ResponseCode code;
    private final Object data;

    public CommonException(ResponseCode code) {
        this.code = code;
        this.data = null;
    }

    public CommonException(ResponseCode code, Object data) {
        this.code = code;
        this.data = data;
    }
}
