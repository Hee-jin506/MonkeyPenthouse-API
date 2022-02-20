package com.monkeypenthouse.core.controller;

import com.monkeypenthouse.core.common.DefaultRes;
import com.monkeypenthouse.core.common.ResponseMessage;
import com.monkeypenthouse.core.dto.TokenDTO;
import com.monkeypenthouse.core.dto.UserDTO.*;
import com.monkeypenthouse.core.security.SecurityUtil;
import com.monkeypenthouse.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity<DefaultRes<?>> getMyUserInfo() throws Exception {
            MyUserResDTO myUser = modelMapper.map(userService.getMyInfo(), MyUserResDTO.class);
            return new ResponseEntity<>(
                    DefaultRes.res(
                            HttpStatus.OK.value(),
                            ResponseMessage.READ_USER,
                            myUser),
                    HttpStatus.OK
            );
    }

    @PostMapping("/reissue")
    public ResponseEntity<DefaultRes<?>> reissue(@RequestHeader("Authorization") String refreshToken) throws Exception {
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.REISSUE_SUCCESS,
                        modelMapper.map(userService.reissue(refreshToken), TokenDTO.ReissueResDTO.class)),
                HttpStatus.OK
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<DefaultRes<?>> logout() throws Exception {
        userService.logout();
        return new ResponseEntity<>(
                DefaultRes.res(
                        HttpStatus.OK.value(),
                        ResponseMessage.LOGOUT_SUCCESS),
                HttpStatus.OK
        );
    }
}
