package com.monkeypenthouse.core.controller.dto.amenity;

import com.monkeypenthouse.core.controller.dto.ticket.TicketSaveReqI;
import com.monkeypenthouse.core.service.dto.amenity.AmenitySaveReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@RequiredArgsConstructor
public class AmenitySaveReqI {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Pattern(regexp = "^.{15,30}$")
    private String title;
    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;
    @NotNull(message = "응원 마감기한은 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
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
    private List<TicketSaveReqI> tickets;
    @NotNull
    private List<MultipartFile> bannerPhotos;
    @NotNull
    private List<MultipartFile> detailPhotos;

    public AmenitySaveReqS toS() {
        return new AmenitySaveReqS(
                title,
                address,
                deadlineDate,
                detail,
                recommended,
                minPersonNum,
                categories,
                tickets.stream().map(TicketSaveReqI::toS).collect(Collectors.toList()),
                bannerPhotos,
                detailPhotos
        );
    }
}