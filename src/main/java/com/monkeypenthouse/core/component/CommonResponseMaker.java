package com.monkeypenthouse.core.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.monkeypenthouse.core.constant.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CommonResponseMaker {

    public <T> CommonResponseEntity makeCommonResponse(final T data, final ResponseCode responseCode) {

        final CommonResponseBody responseBody = new CommonResponseBody(data, responseCode);

        final CommonResponseEntity responseEntity =
                new CommonResponseEntity(responseBody, responseCode.getHttpStatus());

        return responseEntity;
    }

    public CommonResponseEntity makeCommonResponse(final ResponseCode responseCode) {

        final CommonResponseBody responseBody = new CommonResponseBody(responseCode);

        final CommonResponseEntity responseEntity =
                new CommonResponseEntity(responseBody, responseCode.getHttpStatus());

        return responseEntity;
    }

    public class CommonResponseEntity extends ResponseEntity<CommonResponseBody> {

        public CommonResponseEntity(final CommonResponseBody body, final HttpStatus status) {
            super(body, status);
        }
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class CommonResponseBody<T> {

        private final int detailStatus;
        private final String responseMessage;
        private T data;

        private CommonResponseBody(final ResponseCode responseCode) {
            this.detailStatus = responseCode.getDetailStatus();
            this.responseMessage = responseCode.getMessage();
        }

        private CommonResponseBody(final T data, final ResponseCode responseCode) {
            this.detailStatus = responseCode.getDetailStatus();
            this.responseMessage = responseCode.getMessage();
            this.data = data;
        }
    }
}
