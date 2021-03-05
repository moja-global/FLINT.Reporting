/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations;

import global.moja.quantityobservations.models.QuantityObservation;
import global.moja.quantityobservations.util.builders.QuantityObservationBuilder;
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
@ContextConfiguration(initializers = CreateQuantityObservationIT.Initializer.class)
public class CreateQuantityObservationIT {

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final QuantityObservation quantityObservation4;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("quantityObservations")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        quantityObservation4 =
                new QuantityObservationBuilder()
                        .id(4L)
                        .taskId(4L)
                        .partyId(4L)
                        .reportingVariableId(4L)
                        .year(1994)
                        .amount(4.0)
                        .unitId(4L)
                        .version(1)
                        .build();
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
    public void Given_QuantityObservationDetails_When_Post_Then_QuantityObservationRecordWillBeCreatedAndReturned() {

        webTestClient
                .post()
                .uri("/api/v1/quantity_observations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(quantityObservation4), QuantityObservation.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(QuantityObservation.class)
                .value(response -> {
                    Assertions.assertThat(response.getId()).isEqualTo(quantityObservation4.getId());
                    Assertions.assertThat(response.getTaskId()).isEqualTo(quantityObservation4.getTaskId());
                    Assertions.assertThat(response.getPartyId()).isEqualTo(quantityObservation4.getPartyId());
                    Assertions.assertThat(response.getReportingVariableId()).isEqualTo(quantityObservation4.getReportingVariableId());
                    Assertions.assertThat(response.getYear()).isEqualTo(quantityObservation4.getYear());
                    Assertions.assertThat(response.getAmount()).isEqualTo(quantityObservation4.getAmount());
                    Assertions.assertThat(response.getUnitId()).isEqualTo(quantityObservation4.getUnitId());
                    Assertions.assertThat(response.getVersion()).isEqualTo(quantityObservation4.getVersion());
                        }
                );
    }
}
