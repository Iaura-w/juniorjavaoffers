package com.javaoffers.security.login.domain;

import com.javaoffers.security.login.domain.exception.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser registerUser(String username, String password) {

        try {
            AppUser user = new AppUser(username, passwordEncoder.encode(password));
            return userRepository.save(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUsernameException(username);
        }
    }
}