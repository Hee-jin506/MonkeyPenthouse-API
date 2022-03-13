package com.monkeypenthouse.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PageVo<T> {
    private List<T> content;
    private int totalPages;
    private Long totalContents;
    private int size;
    private int page;

    public PageVo(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalContents = page.getTotalElements();
        this.size = page.getNumberOfElements();
        this.page = page.getNumber() + 1;
    }

    public PageVo(Page<?> page, List<T> content) {
        this.content = content;
        this.totalPages = page.getTotalPages();
        this.totalContents = page.getTotalElements();
        this.size = page.getNumberOfElements();
        this.page = page.getNumber() + 1;
    }
}
