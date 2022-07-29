package com.monkeypenthouse.core.service.dto;

import com.monkeypenthouse.core.controller.dto.PageI;
import jdk.jfr.Registered;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class PageS<T> {
    private final List<T> content;
    private final int totalPages;
    private final Long totalContents;
    private final int size;
    private final int page;

    public PageS(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalContents = page.getTotalElements();
        this.size = page.getNumberOfElements();
        this.page = page.getNumber() + 1;
    }

    public PageS(Page<?> page, List<T> content) {
        this.content = content;
        this.totalPages = page.getTotalPages();
        this.totalContents = page.getTotalElements();
        this.size = page.getNumberOfElements();
        this.page = page.getNumber() + 1;
    }
}
