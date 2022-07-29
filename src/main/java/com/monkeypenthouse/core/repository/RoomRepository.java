package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.Room;
import com.monkeypenthouse.core.repository.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE room SET user_id = :userId WHERE user_id IS NULL AND user_role = :userRole ORDER BY RAND() LIMIT 1", nativeQuery = true)
    void updateUserIdForVoidRoom(@Param("userId") Long userId, @Param("userRole") Authority authority);
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE room SET user_id = null WHERE user_id = :userId", nativeQuery = true)
    void deleteUserId(@Param("userId") Long userId);
    Optional<Room> findByUserId(Long userId);

}
