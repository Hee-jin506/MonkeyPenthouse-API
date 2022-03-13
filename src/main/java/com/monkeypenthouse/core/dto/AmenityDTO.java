package com.monkeypenthouse.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class AmenityDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailDTO {
        private Long id;
        private String title;
        private String address;
        private List<String> categories;
        private List<String> bannerImages;
        private List<String> detailImages;
        private LocalDate deadlineDate;
        private LocalDate startDate;
        private String detail;
        private int recommended;
        private int minPersonNum;
        private int maxPersonNum;
        private int currentPersonNum;
        private int status;
        private int fundingPrice;
        private int dibs;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaveReqDTO {
        @NotBlank(message = "제목은 필수 입력값입니다.")
        @Pattern(regexp = "^.{15,30}$")
        private String title;

        @NotBlank(message = "주소는 필수 입력값입니다.")
        private String address;

        @NotNull(message = "응원 마감기한은 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDate deadlineDate;

        @NotBlank(message = "상세 설명은 필수 입력값입니다.")
        @Pattern(regexp = "^.{20,50}$")
        private String detail;

        @NotNull(message = "추천 여부는 필수 입력값입니다.")
        private int recommended;

        @NotNull(message = "최소 인원은 필수 입력값입니다.")
        @Min(value = 0)
        private int minPersonNum;

        @NotNull(message = "카테고리가 한 개 이상 있어야 합니다.")
        private List<String> categories;

        @NotNull(message = "티켓이 한 개 이상 있어야 합니다.")
        private List<TicketDTO.saveDTO> tickets;
    }

}
