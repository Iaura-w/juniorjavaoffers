package com.javaoffers.mongo.config;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.javaoffers.offer.domain.Offer;
import com.javaoffers.offer.domain.OfferRepository;
import com.javaoffers.security.login.domain.AppUser;
import com.javaoffers.security.login.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@ChangeLog(order = "001")
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "seedDatabase", author = "lauur")
    public void seedDatabase(OfferRepository offerRepository) {
        List<Offer> offerList = new ArrayList<>();
        offerList.add(offer1());
        offerList.add(offer2());

        offerRepository.insert(offerList);
    }

    @ChangeSet(order = "002", id = "seedDatabaseUsers", author = "lauur")
    public void seedDatabaseUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        userRepository.insert(admin(passwordEncoder));
    }

    private static AppUser admin(PasswordEncoder passwordEncoder) {
        return new AppUser(null, "admin", passwordEncoder.encode("admin"),new HashSet<>(Collections.singleton("ADMIN")));
    }

    private Offer offer2() {
        return new Offer("2", "XYZ", "Junior Android Developer", "7k - 10k PLN", "https://example.com/offer2");
    }

    private Offer offer1() {
        return new Offer("1", "ABC", "Junior Java Developer", "6k - 8k PLN", "https://example.com/offer1");
    }
}