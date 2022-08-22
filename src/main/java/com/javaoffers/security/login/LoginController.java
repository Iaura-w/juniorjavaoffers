package com.javaoffers.security.login;

import com.javaoffers.security.login.domain.dto.LoginDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginDto loginDto){

    }
}