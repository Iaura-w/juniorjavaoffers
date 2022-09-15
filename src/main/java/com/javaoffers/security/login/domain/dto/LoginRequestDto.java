package com.javaoffers.security.login.domain.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class LoginRequestDto {
    @NotBlank
    String username;
    @NotBlank
    String password;
}
