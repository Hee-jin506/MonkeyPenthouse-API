package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.repository.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
