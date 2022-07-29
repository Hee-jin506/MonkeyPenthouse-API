package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Amenity;
import com.monkeypenthouse.core.repository.entity.Dibs;
import com.monkeypenthouse.core.repository.entity.DibsId;
import com.monkeypenthouse.core.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DibsRepository extends JpaRepository<Dibs, DibsId> {

    Optional<Dibs> findByUserAndAmenity(User user, Amenity amenity);

    List<Dibs> findAllByUser(User user);
}
