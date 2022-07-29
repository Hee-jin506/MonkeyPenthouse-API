package com.monkeypenthouse.core.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class PageI<T> {
    private final List<T> content;
    private final int totalPages;
    private final Long totalContents;
    private final int size;
    private final int page;
}
