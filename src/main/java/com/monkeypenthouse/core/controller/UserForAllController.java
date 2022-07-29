package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.controller.dto.user.*;
import com.monkeypenthouse.core.service.MessageService;
import com.monkeypenthouse.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/user/all/")
@RequiredArgsConstructor
@Validated
public class UserForAllController {

    private final UserService userService;
    private final MessageService messageService;
    private final CommonResponseMaker commonResponseMaker;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseEntity signUp(@RequestBody @Valid UserSignUpReqI request) {
        return commonResponseMaker.makeCommonResponse(
                userService.add(request.toS()).toI(), ResponseCode.SUCCESS);
    }

    /* 회원가입 테스트 용 */
    @DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseEntity delete(
            @RequestParam("email")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
             String email) {
        userService.deleteByEmail(email);
        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }


    @GetMapping(value = "/check-id-duplication")
    public CommonResponseEntity checkIdDuplicate(
            @RequestParam("email")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
            String email) {
        return commonResponseMaker.makeCommonResponse(userService.checkEmailDuplicate(email), ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/sms-auth")
    public CommonResponseEntity smsAuth(@RequestBody UserSendAuthNumReqI request) throws Exception {

        userService.checkPhoneNumDuplicate(request.getPhoneNum());
        messageService.sendAuthNum(request.getPhoneNum());

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/check-sms-auth")
    public CommonResponseEntity checkSmsAuth(@RequestBody UserCheckAuthReqI request) throws Exception {
        return commonResponseMaker.makeCommonResponse(messageService.checkAuthNum(request.toS()), ResponseCode.SUCCESS);
    }

    @PatchMapping(value = "/life-style")
    public CommonResponseEntity updateLifeStyle(@RequestBody @Valid UserUpdateLifeStyleReqI request) throws Exception {
        userService.updateLifeStyle(request.toS());
        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public CommonResponseEntity loginLocal(@RequestBody @Valid UserLoginReqI request) throws Exception {
        return commonResponseMaker.makeCommonResponse(userService.login(request.toS()).toI(), ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/login/kakao")
    @ResponseBody
    public CommonResponseEntity authKakao(@RequestBody String token) throws Exception {
        return commonResponseMaker.makeCommonResponse(userService.authKakao(token).toI(), ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/login/naver")
    @ResponseBody
    public CommonResponseEntity authNaver(@RequestBody String token) throws Exception {
        return commonResponseMaker.makeCommonResponse(userService.authNaver(token).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/find-email")
    public CommonResponseEntity findEmail(@RequestParam("phoneNum") @Pattern(regexp = "^\\d{9,11}$") String phoneNum) throws Exception {
        return commonResponseMaker.makeCommonResponse(userService.findEmail(phoneNum).toI(), ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/check-user")
    public CommonResponseEntity checkUser(UserCheckExistReqI request) throws Exception {
        return commonResponseMaker.makeCommonResponse(userService.checkUser(request.toS()).toI(), ResponseCode.SUCCESS);
    }

}
