package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseBody;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.dto.UserDTO.*;
import com.monkeypenthouse.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/guest/")
@Log4j2
@RequiredArgsConstructor
@Validated
public class UserForGuestController {

    private final UserService userService;
    private final CommonResponseMaker commonResponseMaker;

    @PatchMapping(value = "/password")
    public CommonResponseEntity updatePassword(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestBody @Valid UpdatePWReqDTO userDTO) throws Exception {
        userService.updatePassword(userDetails, userDTO.getPassword());

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }
}
