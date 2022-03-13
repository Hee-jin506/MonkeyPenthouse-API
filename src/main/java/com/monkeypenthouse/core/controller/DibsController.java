package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.common.DefaultRes;
import com.monkeypenthouse.core.common.ResponseMessage;
import com.monkeypenthouse.core.exception.DataNotFoundException;
import com.monkeypenthouse.core.security.PrincipalDetails;
import com.monkeypenthouse.core.service.DibsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Log4j2
@RequiredArgsConstructor
public class DibsController {

    private final DibsService dibsService;

    /**
     * Create Dibs (찜하기 추가)
     */
    @PostMapping("/dibs")
    public ResponseEntity<DefaultRes<?>> createDibs(
            @AuthenticationPrincipal final UserDetails userDetails, @RequestParam final Long amenityId) throws DataNotFoundException {

        dibsService.createDibs(userDetails, amenityId);

        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.CREATED_DIBS),
                HttpStatus.OK
        );
    }

    /**
     * Delete Dibs (찜하기 제거)
     */
    @DeleteMapping("/dibs")
    public ResponseEntity<DefaultRes<?>> deleteDibs(
            @AuthenticationPrincipal final UserDetails userDetails, @RequestParam final Long amenityId) throws DataNotFoundException {

        dibsService.deleteDibs(userDetails, amenityId);

        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.DELETED_DIBS),
                HttpStatus.OK
        );
    }
}
