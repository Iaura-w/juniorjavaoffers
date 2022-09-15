package com.javaoffers.security.jwt;

import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class JwtConfigTest {

    @Bean
    public JwtUtils jwtUtils() {
        JwtUtils mockJwtUtils = mock(JwtUtils.class);
        when(mockJwtUtils.isJwtTokenValid(anyString())).thenReturn(true);
        when(mockJwtUtils.getUsernameFromJwtToken(anyString())).thenReturn("sampleUser");
        return mockJwtUtils;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return new AppUserDetails(new AppUser(null, "sampleUser", "password", new HashSet<>(Collections.singleton("ROLE_ADMIN"))));
            }
        };
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(@Autowired UserDetailsService userDetailsService, @Autowired JwtUtils jwtUtils) {
        return new JwtTokenFilter(userDetailsService, jwtUtils);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return mock(AuthenticationEntryPoint.class);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return mock(JwtAccessDeniedHandler.class);
    }
}