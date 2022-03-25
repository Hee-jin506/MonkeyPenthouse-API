package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.entity.Amenity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AmenityRepository extends CrudRepository<Amenity, Long>, AmenityRepositoryCustom {

    @Query("SELECT DISTINCT a FROM Amenity a join fetch a.photos WHERE a.id=:id")
    Optional<Amenity> findWithPhotosById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Amenity a SET a.status = 2 WHERE (a.status = 0 OR a.status = 1) AND a.deadlineDate < :today")
    void updateStatusByDeadlineDate(@Param("today") LocalDate today);
}
