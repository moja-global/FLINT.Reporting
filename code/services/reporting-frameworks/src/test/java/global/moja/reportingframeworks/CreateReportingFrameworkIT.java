/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks;

import global.moja.reportingframeworks.util.builders.ReportingFrameworkBuilder;
import global.moja.reportingframeworks.models.ReportingFramework;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateReportingFrameworkIT.Initializer.class)
public class CreateReportingFrameworkIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("units")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername()
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @AfterClass
    public static void shutdown() {

        postgreSQLContainer.stop();
    }

    @Test
    public void Given_ReportingFrameworkDetails_When_Post_Then_ReportingFrameworkRecordWillBeCreatedAndReturned() {

        ReportingFramework u =
                new ReportingFrameworkBuilder()
                        .id(4L)
                        .name("Framework 4")
                        .description("Framework 4 Description")
                        .build();

        webTestClient
                .post()
                .uri("/api/v1/reporting_frameworks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(u), ReportingFramework.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ReportingFramework.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(u.getId());
                            Assertions.assertThat(response.getName()).isEqualTo(u.getName());
                            Assertions.assertThat(response.getDescription()).isEqualTo(u.getDescription());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
