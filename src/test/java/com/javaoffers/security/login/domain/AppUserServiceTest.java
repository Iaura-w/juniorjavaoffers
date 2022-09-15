package com.javaoffers.security.login.domain;

import com.javaoffers.security.jwt.JwtUtils;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import com.javaoffers.security.login.domain.exceptions.DuplicateUsernameException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppUserServiceTest {

    private final AppUserRepository userRepository = mock(AppUserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final AppUserService service = new AppUserService(userRepository, passwordEncoder, authenticationManager, jwtUtils);

    @Test
    void should_generate_token_for_user() {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto("user", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        String token = "sampleToken";

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        // when
        String actual = service.generateTokenForUser(loginRequestDto);

        // then
        assertThat(actual).isEqualTo(token);
        verify(authenticationManager, times(1)).authenticate(authentication);
    }

    @Test
    void should_register_new_user() {
        // given
        String username = "newUser";
        String password = "newUser";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        AppUser userToSave = new AppUser(username, encodedPassword);
        AppUser savedUser = new AppUser("1", username, encodedPassword, Collections.singleton("ROLE_USER"));

        when(userRepository.save(userToSave)).thenReturn(savedUser);

        // when
        AppUser actual = service.registerUser(username, password);

        // then
        assertThat(actual).isEqualTo(savedUser);
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void should_throw_duplicate_username_exception_when_username_already_exists() {
        // given
        String username = "duplicateUser";
        String password = "duplicateUser";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        AppUser userToSave = new AppUser(username, encodedPassword);

        when(userRepository.save(userToSave)).thenThrow(DuplicateKeyException.class);

        // when
        // then
        assertThatThrownBy(() -> service.registerUser(username, password))
                .isInstanceOf(DuplicateUsernameException.class)
                .hasMessageContaining(String.format("Username '%s' already exists", username));
        verify(userRepository, times(1)).save(userToSave);
    }
}