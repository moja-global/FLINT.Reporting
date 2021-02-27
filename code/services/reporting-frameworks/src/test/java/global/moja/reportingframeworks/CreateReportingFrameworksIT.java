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
@ContextConfiguration(initializers = CreateReportingFrameworksIT.Initializer.class)
public class CreateReportingFrameworksIT {

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
    public void Given_ReportingFrameworkDetailsList_When_PostAll_Then_ReportingFrameworkRecordsWillBeCreatedAndReturned() {

        ReportingFramework u1 =
                new ReportingFrameworkBuilder()
                        .name("Framework 4")
                        .description("Framework 4 Description")
                        .build();

        ReportingFramework u2 =
                new ReportingFrameworkBuilder()
                        .name("Framework 5")
                        .description("Framework 5 Description")
                        .build();

        ReportingFramework[] reportingFrameworks = new ReportingFramework[]{u1, u2};

        webTestClient
                .post()
                .uri("/api/v1/reporting_frameworks/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reportingFrameworks), ReportingFramework.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(ReportingFramework.class)
                .value(response -> {

                    Assertions.assertThat(response.get(0).getId() == 4L ||
                            response.get(0).getId() == 5L)
                            .isTrue();

                    Assertions.assertThat(response.get(0).getName().equals(u1.getName()) ||
                            response.get(0).getName().equals(u2.getName()))
                            .isTrue();

                    Assertions.assertThat(response.get(0).getVersion() == 1)
                            .isTrue();

                    if (response.get(0).getName().equals(u1.getName())) {
                        Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u1.getDescription());
                    } else if (response.get(0).getName().equals(u2.getName())) {
                        Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u2.getDescription());
                    }


                    Assertions.assertThat(response.get(1).getId() == 4L ||
                            response.get(1).getId() == 5L)
                            .isTrue();

                    Assertions.assertThat(response.get(1).getVersion() == 1)
                            .isTrue();

                    if (response.get(1).getName().equals(u1.getName())) {
                        Assertions.assertThat(response.get(1).getDescription()).isEqualTo(u1.getDescription());
                    } else if (response.get(1).getName().equals(u2.getName())) {
                        Assertions.assertThat(response.get(1).getDescription()).isEqualTo(u2.getDescription());
                    }
                });
    }
}
