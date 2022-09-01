package com.javaoffers.security.jwt;

import com.javaoffers.security.MongoUserService;
import com.javaoffers.security.login.domain.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

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
    public UserDetailsService userDetailsService(@Autowired UserRepository userRepository) {
        return new MongoUserService(userRepository);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(@Autowired UserRepository userRepository) {
        return new JwtTokenFilter(userDetailsService(userRepository), jwtUtils());
    }
}