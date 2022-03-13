package com.monkeypenthouse.core.vo;

import com.monkeypenthouse.core.dto.querydsl.AmenitySimpleDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class GetPageResponseVo extends PageVo<AmenitySimpleVo> {

    public GetPageResponseVo(Page<AmenitySimpleVo> page) {
        super(page);
    }

    public GetPageResponseVo(Page<AmenitySimpleDTO> page, List<AmenitySimpleVo> content) {
        super(page, content);
    }

}
