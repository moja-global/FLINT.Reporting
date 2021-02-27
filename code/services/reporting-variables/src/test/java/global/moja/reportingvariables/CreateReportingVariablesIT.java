/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables;

import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.util.builders.ReportingVariableBuilder;
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

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateReportingVariablesIT.Initializer.class)
public class CreateReportingVariablesIT {

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
    public void Given_ReportingVariableDetailsList_When_PostAll_Then_ReportingVariableRecordsWillBeCreatedAndReturned() {

        ReportingVariable u1 =
                new ReportingVariableBuilder()
                        .reportingFrameworkId(1L)
                        .name("Fourth Variable")
                        .description("Fourth Variable Description")
                        .build();

        ReportingVariable u2 =
                new ReportingVariableBuilder()
                        .reportingFrameworkId(1L)
                        .name("Fifth Variable")
                        .description("Fifth Variable Description")
                        .build();

        ReportingVariable[] reportingVariables= new ReportingVariable[]{u1, u2};

        webTestClient
                .post()
                .uri("/api/v1/reporting_variables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reportingVariables), ReportingVariable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(ReportingVariable.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(u1.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(u1.getName());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u1.getDescription());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(u2.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(u2.getName());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(u2.getDescription());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                });
    }
}
