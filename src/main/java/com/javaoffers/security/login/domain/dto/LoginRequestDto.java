package com.javaoffers.security.login.domain.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Value
public class LoginRequestDto {
    @NotBlank
    @Min(3)
    String username;
    @NotBlank
    @Min(5)
    String password;
}
