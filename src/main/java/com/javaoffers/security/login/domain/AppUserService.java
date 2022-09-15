package com.javaoffers.security.login.domain;

import com.javaoffers.security.jwt.JwtUtils;
import com.javaoffers.security.login.domain.dto.LoginRequestDto;
import com.javaoffers.security.login.domain.exceptions.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public String generateTokenForUser(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(loginRequestDto);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.generateJwtToken(authentication);
    }

    private static UsernamePasswordAuthenticationToken getAuthenticationToken(LoginRequestDto loginRequestDto) {
        return new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
    }

    public AppUser registerUser(String username, String password) {
        try {
            AppUser user = new AppUser(username, passwordEncoder.encode(password));
            return userRepository.save(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUsernameException(username);
        }
    }
}