package com.monkeypenthouse.core.controller.dto.ticket;

import com.monkeypenthouse.core.service.dto.ticket.TicketSaveReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class TicketSaveReqI {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Pattern(regexp = "^.{1,30}$")
    private String name;

    @NotBlank(message = "상세 설명은 필수 입력값입니다.")
    @Pattern(regexp = "^.{1,50}$")
    private String detail;

    @NotNull(message = "정원은 필수 입력값입니다.")
    @Min(value = 0)
    private int capacity;

    @NotNull(message = "가격은 필수 입력값입니다.")
    @Min(value = 0)
    private int price;

    @NotNull(message = "이벤트 날짜는 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime eventDateTime;

    public TicketSaveReqS toS() {
        return new TicketSaveReqS(
                name, detail, capacity, price, eventDateTime
        );
    }
}
