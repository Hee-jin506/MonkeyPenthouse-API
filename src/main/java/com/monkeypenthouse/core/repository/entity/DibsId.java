package com.monkeypenthouse.core.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DibsId implements Serializable {
    private Long user;
    private Long amenity;
}

