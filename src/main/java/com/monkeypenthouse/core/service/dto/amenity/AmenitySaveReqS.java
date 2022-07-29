package com.monkeypenthouse.core.service.dto.amenity;

import com.monkeypenthouse.core.service.dto.ticket.TicketSaveReqS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AmenitySaveReqS {
    private final String title;
    private final String address;
    private final LocalDate deadlineDate;
    private final String detail;
    private final int recommended;
    private final int minPersonNum;
    private final List<String> categories;
    private final List<TicketSaveReqS> tickets;
    private final List<MultipartFile> bannerPhotos;
    private final List<MultipartFile> detailPhotos;
}
