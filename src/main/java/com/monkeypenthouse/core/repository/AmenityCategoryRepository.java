package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.entity.Amenity;
import com.monkeypenthouse.core.entity.AmenityCategory;
import com.monkeypenthouse.core.entity.AmenityCategoryId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmenityCategoryRepository extends CrudRepository<AmenityCategory, AmenityCategoryId> {
    List<AmenityCategory> findAllByAmenity(Amenity amenity);
}
