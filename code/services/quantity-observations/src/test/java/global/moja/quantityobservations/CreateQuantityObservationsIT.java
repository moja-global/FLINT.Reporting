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

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateQuantityObservationsIT.Initializer.class)
public class CreateQuantityObservationsIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final QuantityObservation quantityObservation4;
    static final QuantityObservation quantityObservation5;

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
                        .databaseId(4L)
                        .reportingTableId(4L)
                        .reportingVariableId(4L)
                        .year(1994)
                        .amount(4.0)
                        .unitId(4L)
                        .version(1)
                        .build();

        quantityObservation5 =
                new QuantityObservationBuilder()
                        .id(5L)
                        .taskId(5L)
                        .partyId(5L)
                        .databaseId(5L)
                        .reportingTableId(5L)
                        .reportingVariableId(5L)
                        .year(1995)
                        .amount(5.0)
                        .unitId(5L)
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
    public void Given_QuantityObservationDetailsList_When_PostAll_Then_QuantityObservationRecordsWillBeCreatedAndReturned() {

        QuantityObservation[] quantityObservations= new QuantityObservation[]{quantityObservation4, quantityObservation5};

        webTestClient
                .post()
                .uri("/api/v1/quantity_observations/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(quantityObservations), QuantityObservation.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(QuantityObservation.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(quantityObservation4.getId());
                    Assertions.assertThat(response.get(0).getTaskId()).isEqualTo(quantityObservation4.getTaskId());
                    Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(quantityObservation4.getPartyId());
                    Assertions.assertThat(response.get(0).getDatabaseId()).isEqualTo(quantityObservation4.getDatabaseId());
                    Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(quantityObservation4.getReportingTableId());
                    Assertions.assertThat(response.get(0).getReportingVariableId()).isEqualTo(quantityObservation4.getReportingVariableId());
                    Assertions.assertThat(response.get(0).getYear()).isEqualTo(quantityObservation4.getYear());
                    Assertions.assertThat(response.get(0).getAmount()).isEqualTo(quantityObservation4.getAmount());
                    Assertions.assertThat(response.get(0).getUnitId()).isEqualTo(quantityObservation4.getUnitId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(quantityObservation4.getVersion());

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(quantityObservation5.getId());
                    Assertions.assertThat(response.get(1).getTaskId()).isEqualTo(quantityObservation5.getTaskId());
                    Assertions.assertThat(response.get(1).getPartyId()).isEqualTo(quantityObservation5.getPartyId());
                    Assertions.assertThat(response.get(1).getDatabaseId()).isEqualTo(quantityObservation5.getDatabaseId());
                    Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(quantityObservation5.getReportingTableId());
                    Assertions.assertThat(response.get(1).getReportingVariableId()).isEqualTo(quantityObservation5.getReportingVariableId());
                    Assertions.assertThat(response.get(1).getYear()).isEqualTo(quantityObservation5.getYear());
                    Assertions.assertThat(response.get(1).getAmount()).isEqualTo(quantityObservation5.getAmount());
                    Assertions.assertThat(response.get(1).getUnitId()).isEqualTo(quantityObservation5.getUnitId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(quantityObservation5.getVersion());
                });
    }
}
