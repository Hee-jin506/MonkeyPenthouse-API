package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.common.DefaultRes;
import com.monkeypenthouse.core.common.ResponseMessage;
import com.monkeypenthouse.core.common.SocialLoginRes;
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

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultRes<?>> signUp(@RequestBody @Valid SignupReqDTO userDTO) throws Exception {
        User user = modelMapper.map(userDTO, User.class);
            return new ResponseEntity<>(
                    DefaultRes.res(
                            HttpStatus.CREATED.value(),
                            ResponseMessage.CREATED_USER,
                            modelMapper.map(userService.add(user), SignupResDTO.class)),
                    HttpStatus.CREATED
            );
    }

    /* 회원가입 테스트 용 */
    @DeleteMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultRes<?>> delete(@RequestParam("email") String email) throws Exception {
        userService.deleteByEmail(email);
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.DELETE_USER),
                HttpStatus.OK
        );
    }


    @GetMapping(value = "/check-id-duplication")
    public ResponseEntity<DefaultRes<?>> checkIdDuplicate(@RequestParam("email") String email) throws Exception {
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.READ_USER,
                        userService.checkEmailDuplicate(email)),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/sms-auth")
    public ResponseEntity<DefaultRes<?>> smsAuth(@RequestBody Map<String, String> map) throws Exception {
        String phoneNum = map.get("phoneNum");

        userService.checkPhoneNumDuplicate(phoneNum);
        messageService.sendSMS(phoneNum);
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.SEND_SMS),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/check-sms-auth")
    public ResponseEntity<DefaultRes<?>> checkSmsAuth(@RequestBody Map<String, String> map) throws Exception {
        String phoneNum = map.get("phoneNum");
        String authNum = map.get("authNum");
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.READ_USER,
                        messageService.checkAuthNum(phoneNum, authNum)),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/life-style")
    public ResponseEntity<DefaultRes<?>> updateLifeStyle(@RequestBody @Valid UpdateLSReqDTO userDTO) throws Exception {
        User user = modelMapper.map(userDTO, User.class);
        userService.updateLifeStyle(user);

        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.UPDATE_USER),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<DefaultRes<?>> loginLocal(@RequestBody @Valid LoginReqDTO userDTO) throws Exception {
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

        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.LOGIN_SUCCESS,
                        loginResDTO),
                HttpStatus.OK
        );

    }

    @PostMapping(value = "/login/kakao")
    @ResponseBody
    public ResponseEntity<SocialLoginRes<?>> authKakao(@RequestBody Map<String, String> map) throws Exception {
        String token= map.get("token");
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

        return new ResponseEntity<>(
                SocialLoginRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.LOGIN_SUCCESS,
                        loginResDTO,
                        true),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/login/naver")
    @ResponseBody
    public ResponseEntity<DefaultRes<?>> authNaver(@RequestBody Map<String, String> map) throws Exception {
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

        return new ResponseEntity<>(
                SocialLoginRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.LOGIN_SUCCESS,
                        loginResDTO,
                        true),
                HttpStatus.OK
        );

    }

    @GetMapping(value = "/find-email")
    public ResponseEntity<DefaultRes<?>> findEmail(@RequestParam("phoneNum") @Pattern(regexp = "^\\d{9,11}$") String phoneNum) throws Exception {

        return new ResponseEntity<>(
                    DefaultRes.res(
                            HttpStatus.OK.value(),
                            ResponseMessage.READ_USER,
                            modelMapper.map(userService.findEmail(phoneNum), FindEmailResDTO.class)),
                    HttpStatus.OK
        );
    }

    @GetMapping(value = "/check-user")
    public ResponseEntity<DefaultRes<?>> checkUser(@RequestParam("phoneNum") @Pattern(regexp = "^\\d{9,11}$") String phoneNum,
                                                   @RequestParam("email") @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$") String email) throws Exception {
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.READ_USER,
                        CheckUserResponseDTO.of(userService.checkUser(phoneNum, email))),
                HttpStatus.OK
        );
    }

}
