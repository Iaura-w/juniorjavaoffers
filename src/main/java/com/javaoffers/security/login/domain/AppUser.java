package com.javaoffers.security.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class AppUser {
    @Id
    private String id;
    @Field
    @Indexed(unique = true)
    private String username;
    @Field
    private String password;
    @Field
    private Set<String> roles;
}