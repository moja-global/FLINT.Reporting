/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations;

import global.moja.quantityobservations.models.QuantityObservation;
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
@ContextConfiguration(initializers = UpdateQuantityObservationIT.Initializer.class)
public class UpdateQuantityObservationIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final QuantityObservation quantityObservation1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("quantityObservations")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        quantityObservation1 =
                QuantityObservation.builder()
                        .id(1L)
                        .observationTypeId(10L)
                        .taskId(10L)
                        .partyId(10L)
                        .databaseId(10L)
                        .landUseCategoryId(10L)
                        .reportingTableId(10L)
                        .reportingVariableId(10L)
                        .year(2001)
                        .amount(10.0)
                        .unitId(10L)
                        .version(2)
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
    public void Given_ModifiedDetailsOfAnExistingRecord_When_Put_Then_TheRecordWillBeUpdatedAndReturnedWithItsVersionIncrementedByOne() {

        webTestClient
                .put()
                .uri("/api/v1/quantity_observations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(quantityObservation1), QuantityObservation.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(QuantityObservation.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(quantityObservation1.getId());
                            Assertions.assertThat(response.getObservationTypeId()).isEqualTo(quantityObservation1.getObservationTypeId());
                            Assertions.assertThat(response.getTaskId()).isEqualTo(quantityObservation1.getTaskId());
                            Assertions.assertThat(response.getPartyId()).isEqualTo(quantityObservation1.getPartyId());
                            Assertions.assertThat(response.getDatabaseId()).isEqualTo(quantityObservation1.getDatabaseId());
                            Assertions.assertThat(response.getLandUseCategoryId()).isEqualTo(quantityObservation1.getLandUseCategoryId());
                            Assertions.assertThat(response.getReportingTableId()).isEqualTo(quantityObservation1.getReportingTableId());
                            Assertions.assertThat(response.getReportingVariableId()).isEqualTo(quantityObservation1.getReportingVariableId());
                            Assertions.assertThat(response.getYear()).isEqualTo(quantityObservation1.getYear());
                            Assertions.assertThat(response.getAmount()).isEqualTo(quantityObservation1.getAmount());
                            Assertions.assertThat(response.getUnitId()).isEqualTo(quantityObservation1.getUnitId());
                            Assertions.assertThat(response.getVersion()).isEqualTo(quantityObservation1.getVersion());
                        }
                );
    }
}
