package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Amenity;
import com.monkeypenthouse.core.repository.entity.AmenityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AmenityRepository extends CrudRepository<Amenity, Long>, AmenityRepositoryCustom {

    @Query("SELECT DISTINCT a FROM Amenity a join fetch a.photos WHERE a.id=:id")
    Optional<Amenity> findWithPhotosById(@Param("id") Long id);

    @Query("SELECT distinct a FROM Amenity a join fetch a.tickets")
    List<Amenity> findAllWithTicketsUsingFetchJoin();

    List<Amenity> findAllByDeadlineDate(LocalDate today);

    List<Amenity> findAllByStatus(AmenityStatus status);
}
