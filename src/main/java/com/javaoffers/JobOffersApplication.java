package com.javaoffers;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableMongock
@EnableCaching
public class JobOffersApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobOffersApplication.class, args);
    }
}
