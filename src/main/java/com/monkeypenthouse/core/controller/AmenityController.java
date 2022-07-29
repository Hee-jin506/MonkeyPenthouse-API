package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.controller.dto.amenity.AmenitySaveReqI;
import com.monkeypenthouse.core.service.AmenityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/amenity")
@Log4j2
@RequiredArgsConstructor
public class AmenityController {
    private final AmenityService amenityService;
    private final CommonResponseMaker commonResponseMaker;

    @PostMapping
    public CommonResponseEntity save(@ModelAttribute @Valid AmenitySaveReqI request) throws Exception {
        amenityService.add(request.toS());
        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/{id}")
    public CommonResponseEntity getById(@PathVariable("id") Long id) throws Exception {
        return commonResponseMaker.makeCommonResponse(amenityService.getById(id), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/recently")
    public CommonResponseEntity getPage(Pageable pageable) throws Exception {
        return commonResponseMaker.makeCommonResponse(amenityService.getPage(pageable).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/category")
    public CommonResponseEntity getPageByCategory(@RequestParam("category") Long category, Pageable pageable) throws Exception {
        return commonResponseMaker.makeCommonResponse(
                amenityService.getPageByCategory(category, pageable).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/recommended")
    public CommonResponseEntity getPageByRecommended(Pageable pageable) throws Exception {
        return commonResponseMaker.makeCommonResponse(
                amenityService.getPageByRecommended(pageable).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/dibs")
    public CommonResponseEntity getPageByDibsOn(
            @AuthenticationPrincipal final UserDetails userDetails,
            Pageable pageable) throws Exception {
        return commonResponseMaker.makeCommonResponse(
                amenityService.getAmenitiesDibsOn(userDetails, pageable).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/{id}/tickets")
    public CommonResponseEntity getTicketsOfAmenity(@PathVariable("id") final Long amenityId) {
        return commonResponseMaker.makeCommonResponse(amenityService.getTicketsOfAmenity(amenityId).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/viewed")
    public CommonResponseEntity getViewed(@RequestParam("id") final List<Long> amentiyIds) throws Exception {
        return commonResponseMaker.makeCommonResponse(amenityService.getViewed(amentiyIds).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/ordered")
    public CommonResponseEntity getPageByOrdered(
            @AuthenticationPrincipal final UserDetails userDetails,
            Pageable pageable) throws Exception {
        return commonResponseMaker.makeCommonResponse(
                amenityService.getAmenitiesByOrdered(userDetails, pageable).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/ordered/{id}/tickets")
    public CommonResponseEntity getTicketsOfOrdered(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable("id") final Long amenityId) throws Exception {
        return commonResponseMaker.makeCommonResponse(amenityService.getPurchasesOfOrderedAmenity(userDetails, amenityId).toI(), ResponseCode.SUCCESS);
    }
}
