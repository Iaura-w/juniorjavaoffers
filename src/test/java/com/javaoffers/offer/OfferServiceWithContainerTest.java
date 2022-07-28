package com.javaoffers.offer;

import com.javaoffers.JobOffersApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = JobOffersApplication.class)
@Testcontainers
@ActiveProfiles("container")
public class OfferServiceWithContainerTest implements SampleOfferDto {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    static {
        DB_CONTAINER.start();
        String port = String.valueOf(DB_CONTAINER.getFirstMappedPort());
        System.setProperty("DB_PORT", port);
    }

    @Test
    void test() {

    }

}