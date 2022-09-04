package com.javaoffers.security.login.domain.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class RegisterRequestDto {
    @NotBlank
    @Size(min = 3, max = 50)
    String username;
    @NotBlank
    @Size(min = 3, max = 50)
    String password;
}