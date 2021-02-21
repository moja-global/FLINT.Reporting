/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables;

import moja.global.unfcccvariables.models.UnfcccVariable;
import moja.global.unfcccvariables.util.builders.UnfcccVariableBuilder;
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
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateUnfcccVariablesIT.Initializer.class)
public class CreateUnfcccVariablesIT {

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
    public void Given_UnfcccVariableDetailsList_When_PostAll_Then_UnfcccVariableRecordsWillBeCreatedAndReturned() {

        UnfcccVariable u1 =
                new UnfcccVariableBuilder()
                        .name("Net carbon stock change in organic soils")
                        .measure("Carbon")
                        .abbreviation("C")
                        .unitId(1L)
                        .build();

        UnfcccVariable u2 =
                new UnfcccVariableBuilder()
                        .name("Net carbon stock change in mineral soils")
                        .measure("Carbon")
                        .abbreviation("C")
                        .unitId(1L)
                        .build();

        UnfcccVariable[] unfcccVariables = new UnfcccVariable[]{u1, u2};

        webTestClient
                .post()
                .uri("/api/v1/unfccc_variables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(unfcccVariables), UnfcccVariable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(UnfcccVariable.class)
                .value(response -> {

                    Assertions.assertThat(response.get(0).getId() == 4L || response.get(0).getId() == 5L);
                    Assertions.assertThat(response.get(0).getName().equals("Net carbon stock change in organic soils") || response.get(0).getName().equals("Net carbon stock change in mineral soils"));
                    Assertions.assertThat(response.get(0).getVersion() == 1);

                    if (response.get(0).getName().equals("Net carbon stock change in organic soils")) {
                        Assertions.assertThat(response.get(0).getUnitId())
                                .isEqualTo(u1.getUnitId());
                        Assertions.assertThat(response.get(0).getMeasure())
                                .isEqualTo(u1.getMeasure());
                        Assertions.assertThat(response.get(0).getAbbreviation())
                                .isEqualTo(u1.getAbbreviation());
                    } else if (response.get(0).getName().equals("Net carbon stock change in mineral soils")) {
                        Assertions.assertThat(response.get(0).getUnitId())
                                .isEqualTo(u2.getUnitId());
                        Assertions.assertThat(response.get(0).getMeasure())
                                .isEqualTo(u2.getMeasure());
                        Assertions.assertThat(response.get(0).getAbbreviation())
                                .isEqualTo(u2.getAbbreviation());
                    }

                    Assertions.assertThat(response.get(1).getId() == 4L || response.get(1).getId() == 5L);
                    Assertions.assertThat(response.get(1).getName().equals("Net carbon stock change in organic soils") || response.get(1).getName().equals("Net carbon stock change in mineral soils"));
                    Assertions.assertThat(response.get(1).getVersion() == 1);


                    if (response.get(1).getName().equals("Net carbon stock change in organic soils")) {
                        Assertions.assertThat(response.get(1).getUnitId())
                                .isEqualTo(u1.getUnitId());
                        Assertions.assertThat(response.get(1).getMeasure())
                                .isEqualTo(u1.getMeasure());
                        Assertions.assertThat(response.get(1).getAbbreviation())
                                .isEqualTo(u1.getAbbreviation());
                    }  else if (response.get(1).getName().equals("Net carbon stock change in mineral soils")) {
                        Assertions.assertThat(response.get(1).getUnitId())
                                .isEqualTo(u2.getUnitId());
                        Assertions.assertThat(response.get(1).getMeasure())
                                .isEqualTo(u1.getMeasure());
                        Assertions.assertThat(response.get(1).getAbbreviation())
                                .isEqualTo(u1.getAbbreviation());
                    }
                });
    }
}
