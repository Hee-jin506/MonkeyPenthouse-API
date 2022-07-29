package com.monkeypenthouse.core.controller.dto.amenity;

import com.monkeypenthouse.core.controller.dto.PageI;
import lombok.Getter;

import java.util.List;

@Getter
public class AmenityGetPagesResI extends PageI<AmenitySimpleResI> {
    public AmenityGetPagesResI(List<AmenitySimpleResI> content, int totalPages, Long totalContents, int size, int page) {
        super(content, totalPages, totalContents, size, page);
    }
}
