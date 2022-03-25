package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseBody;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.service.DibsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class DibsController {

    private final DibsService dibsService;
    private final CommonResponseMaker commonResponseMaker;

    /**
     * Create Dibs (찜하기 추가)
     */
    @PostMapping("/dibs")
    public CommonResponseEntity createDibs(
            @AuthenticationPrincipal final UserDetails userDetails, @RequestParam final Long amenityId){

        dibsService.createDibs(userDetails, amenityId);

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    /**
     * Delete Dibs (찜하기 제거)
     */
    @DeleteMapping("/dibs")
    public CommonResponseEntity deleteDibs(
            @AuthenticationPrincipal final UserDetails userDetails, @RequestParam final Long amenityId) {

        dibsService.deleteDibs(userDetails, amenityId);

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }
}
