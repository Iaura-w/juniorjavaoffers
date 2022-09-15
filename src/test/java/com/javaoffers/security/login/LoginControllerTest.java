package com.javaoffers.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaoffers.security.SecurityConfig;
import com.javaoffers.security.jwt.JwtConfigTest;
import com.javaoffers.security.jwt.JwtUtils;
import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.AppUserRepository;
import com.javaoffers.security.login.domain.AppUserService;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import com.javaoffers.security.login.domain.dto.RegisterRequestDto;
import com.javaoffers.security.login.domain.exceptions.DuplicateUsernameException;
import com.javaoffers.security.login.domain.exceptions.LoginControllerErrorHandler;
import com.javaoffers.security.login.domain.exceptions.LoginErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = LoginControllerTest.MockMvcConfigLogin.class)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_and_token_when_valid_credentials() throws Exception {
        // given
        String username = "existingUser";
        String password = "existingUser";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        String body = objectMapper.writeValueAsString(loginRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualAuthorization = mvcResult.getResponse().getHeader("Authorization");
        assertThat(actualAuthorization).isEqualTo("Bearer token");
    }

    @Test
    void should_return_status_unauthorized_when_not_valid_credentials() throws Exception {
        // given
        String username = "nonexistentUser";
        String password = "nonexistentUser";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        String body = objectMapper.writeValueAsString(loginRequestDto);

        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        String expectedMessage = "Bad credentials";

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        LoginErrorResponse actualResponse = objectMapper.readValue(actualBody, LoginErrorResponse.class);

        assertThat(actualResponse.getMessage()).contains(expectedMessage);
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void should_return_status_created_when_new_username() throws Exception {
        // given
        String username = "newUser";
        String password = "newUser";
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(username, password);
        String body = objectMapper.writeValueAsString(registerRequestDto);
        String expected = String.format("User '%s' was registered", username);

        // when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_return_status_conflict_when_duplicate_username() throws Exception {
        // given
        String username = "duplicateUser";
        String password = "duplicateUser";
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(username, password);
        String body = objectMapper.writeValueAsString(registerRequestDto);
        HttpStatus expectedStatus = HttpStatus.CONFLICT;
        String expectedMessage = String.format("Username '%s' already exists", username);

        // when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        LoginErrorResponse actualResponse = objectMapper.readValue(actualBody, LoginErrorResponse.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedMessage);
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedStatus);
    }

    @Import({SecurityConfig.class, JwtConfigTest.class})
    static class MockMvcConfigLogin {

        @Bean
        LoginController loginController(AppUserService appUserService) {
            return new LoginController(appUserService);
        }

        @Bean
        AppUserService appUserService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
            AppUserRepository userRepository = mock(AppUserRepository.class);

            return new AppUserService(userRepository, passwordEncoder, authenticationManager, jwtUtils) {
                @Override
                public String generateTokenForUser(LoginRequestDto loginRequestDto) {
                    if (loginRequestDto.getUsername().contains("existing")) {
                        return "token";
                    }
                    throw new BadCredentialsException("Bad credentials");
                }

                @Override
                public AppUser registerUser(String username, String password) {
                    if (username.contains("new")) {
                        return new AppUser("newUser", "newUser");
                    }
                    throw new DuplicateUsernameException(username);
                }
            };
        }

        @Bean
        LoginControllerErrorHandler loginControllerErrorHandler() {
            return new LoginControllerErrorHandler();
        }
    }
}