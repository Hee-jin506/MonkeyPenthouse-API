package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.component.CommonResponseMaker;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseBody;
import com.monkeypenthouse.core.component.CommonResponseMaker.CommonResponseEntity;
import com.monkeypenthouse.core.constant.ResponseCode;
import com.monkeypenthouse.core.dto.CheckUserResponseDTO;
import com.monkeypenthouse.core.entity.*;
import com.monkeypenthouse.core.dto.UserDTO;
import com.monkeypenthouse.core.dto.UserDTO.*;
import com.monkeypenthouse.core.service.MessageService;
import com.monkeypenthouse.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Map;

@RestController
@RequestMapping("/user/all/")
@Log4j2
@RequiredArgsConstructor
@Validated
public class UserForAllController {

    private final UserService userService;
    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final CommonResponseMaker commonResponseMaker;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseEntity signUp(@RequestBody @Valid SignupReqDTO userDTO) throws Exception {

        final User user = modelMapper.map(userDTO, User.class);

        return commonResponseMaker.makeCommonResponse(
                modelMapper.map(userService.add(user), SignupResDTO.class),
                ResponseCode.SUCCESS);
    }

    /* 회원가입 테스트 용 */
    @DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseEntity delete(@RequestParam("email") String email) throws Exception {
        userService.deleteByEmail(email);

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }


    @GetMapping(value = "/check-id-duplication")
    public CommonResponseEntity checkIdDuplicate(@RequestParam("email") String email) throws Exception {

        return commonResponseMaker.makeCommonResponse(userService.checkEmailDuplicate(email), ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/sms-auth")
    public CommonResponseEntity smsAuth(@RequestBody Map<String, String> map) throws Exception {

        final String phoneNum = map.get("phoneNum");

        userService.checkPhoneNumDuplicate(phoneNum);
        messageService.sendSMS(phoneNum);

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/check-sms-auth")
    public CommonResponseEntity checkSmsAuth(@RequestBody Map<String, String> map) throws Exception {

        final String phoneNum = map.get("phoneNum");
        final String authNum = map.get("authNum");

        return commonResponseMaker.makeCommonResponse(
                messageService.checkAuthNum(phoneNum, authNum),
                ResponseCode.SUCCESS);
    }

    @PatchMapping(value = "/life-style")
    public CommonResponseEntity updateLifeStyle(@RequestBody @Valid UpdateLSReqDTO userDTO) throws Exception {

        final User user = modelMapper.map(userDTO, User.class);

        userService.updateLifeStyle(user);

        return commonResponseMaker.makeCommonResponse(ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public CommonResponseEntity loginLocal(@RequestBody @Valid LoginReqDTO userDTO) throws Exception {
        User user = modelMapper.map(userDTO, User.class);

        Map<String, Object> map = userService.login(user);
        User loggedInUser = (User) map.get("user");
        Tokens tokens = (Tokens) map.get("tokens");

        UserDTO.LoginResDTO loginResDTO = modelMapper.map(loggedInUser, UserDTO.LoginResDTO.class);
        loginResDTO.setGrantType(tokens.getGrantType());
        loginResDTO.setAccessToken(tokens.getAccessToken());
        loginResDTO.setAccessTokenExpiresIn(tokens.getAccessTokenExpiresIn());
        loginResDTO.setRefreshToken(tokens.getRefreshToken());
        loginResDTO.setRefreshTokenExpiresIn(tokens.getRefreshTokenExpiresIn());

        return commonResponseMaker.makeCommonResponse(loginResDTO, ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/login/kakao")
    @ResponseBody
    public CommonResponseEntity authKakao(@RequestBody Map<String, String> map) throws Exception {

        String token = map.get("token");
        User user = userService.authKakao(token);
        // 유저 정보가 있으면 로그인 처리
        Map<String, Object> result = userService.login(user);
        User loggedInUser = (User) result.get("user");
        Tokens tokens = (Tokens) result.get("tokens");

        // 토큰 정보가 있으면 토큰 정보 전송 및 로그인 완료 처리
        UserDTO.LoginResDTO loginResDTO = modelMapper.map(loggedInUser, UserDTO.LoginResDTO.class);
        loginResDTO.setGrantType(tokens.getGrantType());
        loginResDTO.setAccessToken(tokens.getAccessToken());
        loginResDTO.setAccessTokenExpiresIn(tokens.getAccessTokenExpiresIn());
        loginResDTO.setRefreshToken(tokens.getRefreshToken());
        loginResDTO.setRefreshTokenExpiresIn(tokens.getRefreshTokenExpiresIn());

        return commonResponseMaker.makeCommonResponse(loginResDTO, ResponseCode.SUCCESS);
    }

    @PostMapping(value = "/login/naver")
    @ResponseBody
    public CommonResponseEntity authNaver(@RequestBody Map<String, String> map) throws Exception {
        String token = map.get("token");
        User user = userService.authNaver(token);
        // 유저 정보가 있으면 로그인 처리
        Map<String, Object> result = userService.login(user);
        User loggedInUser = (User) result.get("user");
        Tokens tokens = (Tokens) result.get("tokens");

        // 토큰 정보가 있으면 토큰 정보 전송 및 로그인 완료 처리
        UserDTO.LoginResDTO loginResDTO = modelMapper.map(loggedInUser, UserDTO.LoginResDTO.class);
        loginResDTO.setGrantType(tokens.getGrantType());
        loginResDTO.setAccessToken(tokens.getAccessToken());
        loginResDTO.setAccessTokenExpiresIn(tokens.getAccessTokenExpiresIn());
        loginResDTO.setRefreshToken(tokens.getRefreshToken());
        loginResDTO.setRefreshTokenExpiresIn(tokens.getRefreshTokenExpiresIn());

        return commonResponseMaker.makeCommonResponse(loginResDTO, ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/find-email")
    public CommonResponseEntity findEmail(@RequestParam("phoneNum") @Pattern(regexp = "^\\d{9,11}$") String phoneNum) throws Exception {

        return commonResponseMaker.makeCommonResponse(
                modelMapper.map(userService.findEmail(phoneNum), FindEmailResDTO.class),
                ResponseCode.SUCCESS);
    }

    @GetMapping(value = "/check-user")
    public CommonResponseEntity checkUser(@RequestParam("phoneNum") @Pattern(regexp = "^\\d{9,11}$") String phoneNum,
                                          @RequestParam("email") @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$") String email) throws Exception {

        final CheckUserResponseDTO responseDTO = CheckUserResponseDTO.of(userService.checkUser(phoneNum, email));

        return commonResponseMaker.makeCommonResponse(responseDTO, ResponseCode.SUCCESS);
    }

}
