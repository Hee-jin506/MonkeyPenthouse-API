package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Amenity;
import com.monkeypenthouse.core.repository.entity.AmenityCategory;
import com.monkeypenthouse.core.repository.entity.AmenityCategoryId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AmenityCategoryRepository extends CrudRepository<AmenityCategory, AmenityCategoryId> {
    List<AmenityCategory> findAllByAmenity(Amenity amenity);
}
