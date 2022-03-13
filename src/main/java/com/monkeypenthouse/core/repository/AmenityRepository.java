package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.entity.Amenity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AmenityRepository extends CrudRepository<Amenity, Long>, AmenityRepositoryCustom {

    @Query("SELECT DISTINCT a FROM Amenity a join fetch a.photos WHERE a.id=:id")
    Optional<Amenity> findWithPhotosById(@Param("id") Long id);

}
