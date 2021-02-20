/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes;

import moja.global.fluxtypes.models.FluxType;
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
@ContextConfiguration(initializers = RetrieveAllFluxTypesIT.Initializer.class)
public class RetrieveAllFluxTypesIT {

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
    public void Given_UnitRecordsExist_When_GetAllWithoutIdsFilter_Then_AllUnitRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri("/api/v1/flux_types/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxType.class)
                .value(response -> {
                            Assertions.assertThat(response.get(0).getId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(0).getName())
                                    .isEqualTo("Wildfire");
                            Assertions.assertThat(response.get(0).getDescription())
                                    .isEqualTo("Wildfire Flux Type Description");
                            Assertions.assertThat(response.get(0).getVersion())
                                    .isEqualTo(1);
                            Assertions.assertThat(response.get(1).getId())
                                    .isEqualTo(2L);
                            Assertions.assertThat(response.get(1).getName())
                                    .isEqualTo("Harvest");
                            Assertions.assertThat(response.get(1).getDescription())
                                    .isEqualTo("Harvest Flux Type Description");
                            Assertions.assertThat(response.get(1).getVersion())
                                    .isEqualTo(1);
                            Assertions.assertThat(response.get(2).getId())
                                    .isEqualTo(3L);
                            Assertions.assertThat(response.get(2).getName())
                                    .isEqualTo("Plough");
                            Assertions.assertThat(response.get(2).getDescription())
                                    .isEqualTo("Plough Flux Type Description");
                            Assertions.assertThat(response.get(2).getVersion())
                                    .isEqualTo(1);
                        }
                );
    }
}
