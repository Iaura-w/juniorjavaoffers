package com.javaoffers.security.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaoffers.JobOffersApplication;
import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.AppUserRepository;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LoginAndOffersIntegrationTest.TestConfig.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("container")
class LoginAndOffersIntegrationTest {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    static {
        DB_CONTAINER.start();
        String port = DB_CONTAINER.getFirstMappedPort().toString();
        System.setProperty("DB_PORT", port);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(existingUser());
        userRepository.save(admin());
    }

    @Test
    void should_return_offers_status_ok_when_get_after_login_valid_user() throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String username = "existingUser";
        assertThat(userRepository.findByUsername(username)).isPresent();
        String body = existingUserBody();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(authorizationHeader);

        // then
        mockMvc.perform(get("/api/offers")
                        .header(authorizationHeader, token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_unauthorized_when_get_after_login_invalid_user() throws Exception {
        // given
        String username = "nonexistentUser";
        assertThat(userRepository.findByUsername(username)).isNotPresent();
        String body = nonexistentUserBody();

        // when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());

        // then
        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_ok_when_get_after_login_admin() throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String username = "admin";
        assertThat(userRepository.findByUsername(username)).isPresent();
        String body = adminBody();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(authorizationHeader);

        // then
        mockMvc.perform(get("/api/offers")
                        .header(authorizationHeader, token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_no_content_when_delete_after_login_admin() throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String username = "admin";
        assertThat(userRepository.findByUsername(username)).isPresent();
        String body = adminBody();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(authorizationHeader);

        // then
        mockMvc.perform(delete("/api/offers/33")
                        .header(authorizationHeader, token))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_forbidden_when_delete_after_login_user() throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String username = "existingUser";
        assertThat(userRepository.findByUsername(username)).isPresent();
        String body = existingUserBody();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(authorizationHeader);

        // then
        mockMvc.perform(delete("/api/offers/33")
                        .header(authorizationHeader, token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_unauthorized_when_delete_after_login_invalid_user() throws Exception {
        // given
        String username = "nonexistentUser";
        assertThat(userRepository.findByUsername(username)).isNotPresent();
        String body = nonexistentUserBody();

        // when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());


        // then
        mockMvc.perform(delete("/api/offers/33"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_bad_request_when_post_after_login_admin() throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String username = "admin";
        assertThat(userRepository.findByUsername(username)).isPresent();
        String body = adminBody();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(authorizationHeader);

        // then
        mockMvc.perform(post("/api/offers")
                        .header(authorizationHeader, token))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_forbidden_when_post_after_login_user() throws Exception {
        // given
        String authorizationHeader = "Authorization";
        String username = "existingUser";
        assertThat(userRepository.findByUsername(username)).isPresent();
        String body = existingUserBody();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(authorizationHeader);

        // then
        mockMvc.perform(post("/api/offers")
                        .header(authorizationHeader, token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void should_return_offers_status_unauthorized_when_post_after_login_invalid_user() throws Exception {
        // given
        String username = "nonexistentUser";
        assertThat(userRepository.findByUsername(username)).isNotPresent();
        String body = nonexistentUserBody();

        // when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());


        // then
        mockMvc.perform(post("/api/offers"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private String existingUserBody() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new LoginRequestDto("existingUser", "existingUser"));
    }

    private String nonexistentUserBody() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new LoginRequestDto("nonexistentUser", "nonexistentUser"));
    }

    private String adminBody() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new LoginRequestDto("admin", "password"));
    }

    private AppUser existingUser() {
        return new AppUser("existingUser", passwordEncoder.encode("existingUser"));
    }

    private AppUser admin() {
        return new AppUser("idAdmin", "admin", passwordEncoder.encode("password"), Collections.singleton("ROLE_ADMIN"));
    }

    @Import(JobOffersApplication.class)
    static class TestConfig {

    }
}