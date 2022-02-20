package com.monkeypenthouse.core.repository;

import com.monkeypenthouse.core.dao.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
