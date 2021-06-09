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

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = UpdateQuantityObservationsIT.Initializer.class)
public class UpdateQuantityObservationsIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final QuantityObservation quantityObservation1;
    static final QuantityObservation quantityObservation2;

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

        quantityObservation2 =
                QuantityObservation.builder()
                        .id(2L)
                        .observationTypeId(20L)
                        .taskId(20L)
                        .partyId(20L)
                        .databaseId(20L)
                        .landUseCategoryId(20L)
                        .reportingTableId(20L)
                        .reportingVariableId(20L)
                        .year(2002)
                        .amount(20.0)
                        .unitId(20L)
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
    public void Given_ModifiedDetailsOfExistingRecords_When_PutAll_Then_TheRecordsWillBeUpdatedAndReturnedWithTheirVersionsIncrementedByOne() {


        QuantityObservation[] quantityObservations = new QuantityObservation[]{quantityObservation1, quantityObservation2};

        webTestClient
                .put()
                .uri("/api/v1/quantity_observations/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(quantityObservations), QuantityObservation.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(QuantityObservation.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(quantityObservation1.getId());
                            Assertions.assertThat(response.get(0).getObservationTypeId()).isEqualTo(quantityObservation1.getObservationTypeId());
                            Assertions.assertThat(response.get(0).getTaskId()).isEqualTo(quantityObservation1.getTaskId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(quantityObservation1.getPartyId());
                            Assertions.assertThat(response.get(0).getDatabaseId()).isEqualTo(quantityObservation1.getDatabaseId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(quantityObservation1.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(quantityObservation1.getReportingTableId());
                            Assertions.assertThat(response.get(0).getReportingVariableId()).isEqualTo(quantityObservation1.getReportingVariableId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(quantityObservation1.getYear());
                            Assertions.assertThat(response.get(0).getAmount()).isEqualTo(quantityObservation1.getAmount());
                            Assertions.assertThat(response.get(0).getUnitId()).isEqualTo(quantityObservation1.getUnitId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(quantityObservation1.getVersion());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(quantityObservation2.getId());
                            Assertions.assertThat(response.get(1).getObservationTypeId()).isEqualTo(quantityObservation2.getObservationTypeId());
                            Assertions.assertThat(response.get(1).getTaskId()).isEqualTo(quantityObservation2.getTaskId());
                            Assertions.assertThat(response.get(1).getPartyId()).isEqualTo(quantityObservation2.getPartyId());
                            Assertions.assertThat(response.get(1).getDatabaseId()).isEqualTo(quantityObservation2.getDatabaseId());
                            Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(quantityObservation2.getLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(quantityObservation2.getReportingTableId());
                            Assertions.assertThat(response.get(1).getReportingVariableId()).isEqualTo(quantityObservation2.getReportingVariableId());
                            Assertions.assertThat(response.get(1).getYear()).isEqualTo(quantityObservation2.getYear());
                            Assertions.assertThat(response.get(1).getAmount()).isEqualTo(quantityObservation2.getAmount());
                            Assertions.assertThat(response.get(1).getUnitId()).isEqualTo(quantityObservation2.getUnitId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(quantityObservation2.getVersion());


                        }
                );
    }
}
