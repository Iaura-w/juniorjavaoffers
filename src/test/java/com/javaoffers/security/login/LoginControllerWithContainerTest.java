package com.javaoffers.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaoffers.JobOffersApplication;
import com.javaoffers.exceptions.ValidationErrorResponse;
import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.AppUserRepository;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import com.javaoffers.security.login.domain.dto.RegisterRequestDto;
import com.javaoffers.security.login.domain.exceptions.LoginErrorResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LoginControllerWithContainerTest.TestConfig.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("container")
public class LoginControllerWithContainerTest {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static {
        DB_CONTAINER.start();
        String port = DB_CONTAINER.getFirstMappedPort().toString();
        System.setProperty("DB_PORT", port);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void should_return_status_ok_and_token_when_login_with_valid_credentials(@Autowired PasswordEncoder passwordEncoder) throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String tokenPrefix = "Bearer ";
        String username = "existingUser";
        String password = "existingUser";

        AppUser existingUser = new AppUser(username, passwordEncoder.encode(password));
        userRepository.save(existingUser);
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        String body = objectMapper.writeValueAsString(loginRequestDto);

        assertThat(userRepository.findByUsername(username)).isPresent();

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualHeader = mvcResult.getResponse().getHeader(authorizationHeader);
        assertThat(actualHeader).contains(tokenPrefix);
    }

    @Test
    void should_return_status_unauthorized_when_login_with_invalid_password(@Autowired PasswordEncoder passwordEncoder) throws Exception {
        // given
        String username = "existingUser";
        String password = "existingUser";
        String badPassword = "badPassword";

        AppUser existingUser = new AppUser(username, passwordEncoder.encode(password));
        userRepository.save(existingUser);
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, badPassword);
        String body = objectMapper.writeValueAsString(loginRequestDto);

        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        String expectedMessage = "Bad credentials";

        assertThat(userRepository.findByUsername(username)).isPresent();

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        LoginErrorResponse actualResponse = objectMapper.readValue(actualBody, LoginErrorResponse.class);

        assertThat(actualResponse.getMessage()).contains(expectedMessage);
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void should_return_status_unauthorized_when_login_and_user_does_not_exists() throws Exception {
        // given
        String username = "nonexistentUser";
        String password = "nonexistentUser";

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        String body = objectMapper.writeValueAsString(loginRequestDto);

        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        String expectedMessage = "Bad credentials";

        assertThat(userRepository.findByUsername(username)).isNotPresent();

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        LoginErrorResponse actualResponse = objectMapper.readValue(actualBody, LoginErrorResponse.class);

        assertThat(actualResponse.getMessage()).contains(expectedMessage);
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void should_return_status_bad_request_when_login_with_no_username() throws Exception {
        // given
        String username = "";
        String password = "password";

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        String body = objectMapper.writeValueAsString(loginRequestDto);
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        String expectedMessage = "[username - must not be blank]";

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        ValidationErrorResponse actualResponse = objectMapper.readValue(actualBody, ValidationErrorResponse.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedMessage);
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void should_return_status_bad_request_when_login_with_no_password() throws Exception {
        // given
        String username = "username";
        String password = "  ";

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
        String body = objectMapper.writeValueAsString(loginRequestDto);
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        String expectedMessage = "[password - must not be blank]";

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        ValidationErrorResponse actualResponse = objectMapper.readValue(actualBody, ValidationErrorResponse.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedMessage);
        assertThat(actualResponse.getHttpStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void should_return_status_created_when_register_valid_new_user() throws Exception {
        // given
        String username = "newUser";
        String password = "newUser";
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(username, password);
        String body = objectMapper.writeValueAsString(registerRequestDto);
        String expectedMessage = String.format("User '%s' was registered", username);
        assertThat(userRepository.findByUsername(username)).isNotPresent();

        // when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualBody).isEqualTo(expectedMessage);
        assertThat(userRepository.findByUsername(username)).isPresent();
    }

    @Import(JobOffersApplication.class)
    static class TestConfig {

    }
}