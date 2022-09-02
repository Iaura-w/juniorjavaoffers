package com.javaoffers.security.login;

import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.UserService;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.generateTokenForUser(loginRequestDto);
        log.info("User '{}' logged in", loginRequestDto.getUsername());
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AppUser registeredUser = userService.registerUser(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        log.info("New user '{}' registered", loginRequestDto.getUsername());
        return ResponseEntity.ok(String.format("User '%s' was registered", registeredUser.getUsername()));
    }
}