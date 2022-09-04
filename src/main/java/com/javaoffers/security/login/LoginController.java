package com.javaoffers.security.login;

import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.AppUserService;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import com.javaoffers.security.login.domain.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final AppUserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.generateTokenForUser(loginRequestDto);
        log.info("User '{}' logged in", loginRequestDto.getUsername());
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        AppUser registeredUser = userService.registerUser(registerRequestDto.getUsername(), registerRequestDto.getPassword());
        log.info("New user '{}' registered", registerRequestDto.getUsername());
        return ResponseEntity.ok(String.format("User '%s' was registered", registeredUser.getUsername()));
    }
}