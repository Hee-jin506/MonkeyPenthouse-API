package com.monkeypenthouse.core.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmenityCategoryId implements Serializable {
    private Long amenity;
    private Long category;
}
