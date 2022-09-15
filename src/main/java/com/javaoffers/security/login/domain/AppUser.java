package com.javaoffers.security.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.javaoffers.security.login.domain.AppUserRole.USER;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class AppUser {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 3, max = 50)
    private String password;

    private Set<String> roles = new HashSet<>();

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Collections.singleton("ROLE_" + USER.name());
    }
}