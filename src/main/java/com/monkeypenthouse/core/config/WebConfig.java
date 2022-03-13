package com.monkeypenthouse.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePage() {
        return p -> {
            p.setOneIndexedParameters(true);
            p.setFallbackPageable(PageRequest.of(0, 5));
            p.setMaxPageSize(10);
        };
    }
    @Bean
    public SortHandlerMethodArgumentResolverCustomizer customizeSort() {
        return s -> {
            s.setFallbackSort(Sort.by("createdAt").descending());
        };
    }
}
