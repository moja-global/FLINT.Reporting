/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables;

import moja.global.unfcccvariables.models.UnfcccVariable;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveAllUnfcccVariablesIT.Initializer.class)
public class RetrieveAllUnfcccVariablesIT {

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
    public void Given_UnfcccVariableRecordsExist_When_GetAllWithoutIdsFilter_Then_AllUnfcccVariableRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri("/api/v1/unfccc_variables/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UnfcccVariable.class)
                .value(response -> {
                            Assertions.assertThat(response.get(0).getId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(0).getName())
                                    .isEqualTo("Net carbon stock change in living biomass");
                            Assertions.assertThat(response.get(0).getMeasure())
                                    .isEqualTo("Carbon");
                            Assertions.assertThat(response.get(0).getAbbreviation())
                                    .isEqualTo("C");
                            Assertions.assertThat(response.get(0).getUnitId())
                                    .isEqualTo(6L);
                            Assertions.assertThat(response.get(0).getVersion())
                                    .isEqualTo(1);
                            Assertions.assertThat(response.get(1).getId())
                                    .isEqualTo(2L);
                            Assertions.assertThat(response.get(1).getName())
                                    .isEqualTo("Net carbon stock change in dead organic matter");
                            Assertions.assertThat(response.get(1).getMeasure())
                                    .isEqualTo("Carbon");
                            Assertions.assertThat(response.get(1).getAbbreviation())
                                    .isEqualTo("C");
                            Assertions.assertThat(response.get(1).getUnitId())
                                    .isEqualTo(6L);
                            Assertions.assertThat(response.get(1).getVersion())
                                    .isEqualTo(1);
                            Assertions.assertThat(response.get(2).getId())
                                    .isEqualTo(3L);
                            Assertions.assertThat(response.get(2).getName())
                                    .isEqualTo("Net carbon stock change in mineral soils");
                            Assertions.assertThat(response.get(2).getMeasure())
                                    .isEqualTo("Carbon");
                            Assertions.assertThat(response.get(2).getAbbreviation())
                                    .isEqualTo("C");
                            Assertions.assertThat(response.get(2).getUnitId())
                                    .isEqualTo(6L);
                            Assertions.assertThat(response.get(2).getVersion())
                                    .isEqualTo(1);
                        }
                );
    }
}
