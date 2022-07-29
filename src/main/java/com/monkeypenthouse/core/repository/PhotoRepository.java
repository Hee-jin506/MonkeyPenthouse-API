package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Amenity;
import com.monkeypenthouse.core.repository.entity.Photo;
import com.monkeypenthouse.core.repository.entity.PhotoType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
    List<Photo> findAllByAmenity(Amenity amenity);
    List<Photo> findAllByType(PhotoType type);
}
