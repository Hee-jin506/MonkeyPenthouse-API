package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.exception.DataNotFoundException;
import com.monkeypenthouse.core.exception.DibsDuplicatedException;
import com.monkeypenthouse.core.security.PrincipalDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface DibsService {
    void createDibs(UserDetails userDetails, Long amenityId) throws DataNotFoundException, DibsDuplicatedException;

    void deleteDibs(UserDetails userDetails, Long dibsId) throws DataNotFoundException;
}
