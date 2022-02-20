package com.monkeypenthouse.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.monkeypenthouse.core.common.DefaultRes;

@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/")
    public ResponseEntity<DefaultRes<?>> home() {
        return new ResponseEntity<>(
                DefaultRes.res(HttpStatus.OK.value(), "hello world!"),
                HttpStatus.OK
        );
    }

}
