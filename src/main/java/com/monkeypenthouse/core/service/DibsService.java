package com.monkeypenthouse.core.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface DibsService {
    void createDibs(UserDetails userDetails, Long amenityId);

    void deleteDibs(UserDetails userDetails, Long dibsId);
}
