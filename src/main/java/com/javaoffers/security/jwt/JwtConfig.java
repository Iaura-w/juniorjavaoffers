package com.javaoffers.security.jwt;

import com.javaoffers.security.MongoUserService;
import com.javaoffers.security.login.domain.AppUserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@Data
public class JwtConfig {
    @Value("${security.jwt.config.tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(tokenExpirationAfterDays);
    }

    @Bean
    public UserDetailsService userDetailsService(@Autowired AppUserRepository userRepository) {
        return new MongoUserService(userRepository);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(@Autowired AppUserRepository userRepository) {
        return new JwtTokenFilter(userDetailsService(userRepository), jwtUtils());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }
}