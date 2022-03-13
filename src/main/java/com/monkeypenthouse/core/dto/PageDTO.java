package com.monkeypenthouse.core.dto;

import com.monkeypenthouse.core.vo.AmenitySimpleVo;
import com.monkeypenthouse.core.vo.GetTicketsOfAmenityResponseVo;
import com.monkeypenthouse.core.vo.PageVo;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class PageDTO<T> {
    private List<T> content;
    private int totalPages;
    private Long totalContents;
    private int size;
    private int page;
}
