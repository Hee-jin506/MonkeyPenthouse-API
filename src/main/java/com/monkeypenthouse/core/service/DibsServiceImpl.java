package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.entity.Amenity;
import com.monkeypenthouse.core.entity.Dibs;
import com.monkeypenthouse.core.entity.DibsId;
import com.monkeypenthouse.core.entity.User;
import com.monkeypenthouse.core.exception.CommonException;
import com.monkeypenthouse.core.repository.AmenityRepository;
import com.monkeypenthouse.core.repository.DibsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DibsServiceImpl implements DibsService {

    private final UserService userService;
    private final AmenityRepository amenityRepository;
    private final DibsRepository dibsRepository;

    @Override
    @Transactional
    public void createDibs(final UserDetails userDetails, final Long amenityId){
        final User user = userService.getUserByEmail(userDetails.getUsername());

        final Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));

        dibsRepository.findByUserAndAmenity(user, amenity).ifPresent((d) -> {
                throw new CommonException(ResponseCode.DATA_DUPLICATED);
        });

        final Dibs dibs = new Dibs(user, amenity);

        dibsRepository.save(dibs);
    }

    @Override
    @Transactional
    public void deleteDibs(UserDetails userDetails, Long amenityId) {
        final User user = userService.getUserByEmail(userDetails.getUsername());

        final DibsId dibsId = new DibsId(user.getId(), amenityId);

        final Dibs dibs = dibsRepository.findById(dibsId)
                .orElseThrow(() -> new CommonException(ResponseCode.DATA_NOT_FOUND));

        dibsRepository.delete(dibs);
    }
}
