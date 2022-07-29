package com.monkeypenthouse.core.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface DibsService {
    void createDibs(final UserDetails userDetails, final Long amenityId);

    void deleteDibs(final UserDetails userDetails, final Long dibsId);
}
