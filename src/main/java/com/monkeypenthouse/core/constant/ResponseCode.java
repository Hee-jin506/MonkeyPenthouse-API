package com.monkeypenthouse.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS(0, "Success", HttpStatus.OK),

    USER_NOT_FOUND(1000, "회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    DATA_NOT_FOUND(2000, "데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DATA_DUPLICATED(2001, "데이터가 중복되었습니다.", HttpStatus.CONFLICT),

    SOCIAL_AUTH_FAILED(3000, "소셜 인증 실패했습니다.", HttpStatus.UNAUTHORIZED),
    ADDITIONAL_INFO_NEEDED(3001, "추가 정보 입력이 필요합니다.", HttpStatus.UNAUTHORIZED),

    EMPTY_ROOM_NOT_EXISTED(4000, "빈 방이 없습니다.", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_DUPLICATED(4001, "이미 가입된 회원의 전화번호입니다.", HttpStatus.FORBIDDEN),
    PASSWORD_NOT_MATCHED(4002, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    LIFE_STYLE_TEST_NEEDED(4003, "라이프스타일 테스트 미완료 회원입니다.", HttpStatus.UNAUTHORIZED),

    DATA_INTEGRITY_VIOLATED(9000, "중복된 데이터가 추가 요청되었습니다.", HttpStatus.CONFLICT),
    AUTHENTICATION_FAILED(9001, "인증 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    REISSUE_FAILED(9002, "토큰 재발급에 실패하였습니다.", HttpStatus.UNAUTHORIZED),

    HTTP_MESSAGE_NOT_READABLE(9010, "JSON 파싱에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_NOT_VALID(9011, "JSON Body가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATED(9012, "요청에 포함된 파라미터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(9999, "서버 내부에서 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final Integer detailStatus;
    private final String message;
    private final HttpStatus httpStatus;

}
