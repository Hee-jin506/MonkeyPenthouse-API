package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.entity.Amenity;
import com.monkeypenthouse.core.entity.Dibs;
import com.monkeypenthouse.core.entity.DibsId;
import com.monkeypenthouse.core.entity.User;
import com.monkeypenthouse.core.exception.DataNotFoundException;
import com.monkeypenthouse.core.exception.DibsDuplicatedException;
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
    public void createDibs(final UserDetails userDetails, final Long amenityId) throws DataNotFoundException {
        final User user = userService.getUserByEmail(userDetails.getUsername());

        final Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new DataNotFoundException(Amenity.builder().id(amenityId).build()));

        dibsRepository.findByUserAndAmenity(user, amenity).ifPresent((d) -> {
                throw new DibsDuplicatedException();
        });

        final Dibs dibs = new Dibs(user, amenity);

        dibsRepository.save(dibs);
    }

    @Override
    @Transactional
    public void deleteDibs(UserDetails userDetails, Long amenityId) throws DataNotFoundException {
        final User user = userService.getUserByEmail(userDetails.getUsername());

        final DibsId dibsId = new DibsId(user.getId(), amenityId);

        final Dibs dibs = dibsRepository.findById(dibsId)
                .orElseThrow(() -> new DataNotFoundException(Dibs.builder().user(user).build()));

        dibsRepository.delete(dibs);
    }
}
