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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveQuantityObservationsIT.Initializer.class)
public class RetrieveQuantityObservationsIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final QuantityObservation quantityObservation1;
    static final QuantityObservation quantityObservation2;
    static final QuantityObservation quantityObservation3;

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
                        .observationTypeId(1L)
                        .taskId(1L)
                        .partyId(1L)
                        .databaseId(1L)
                        .landUseCategoryId(1L)
                        .reportingTableId(1L)
                        .reportingVariableId(1L)
                        .year(1991)
                        .amount(1.0)
                        .unitId(1L)
                        .version(1)
                        .build();

        quantityObservation2 =
                QuantityObservation.builder()
                        .id(2L)
                        .observationTypeId(2L)
                        .taskId(2L)
                        .partyId(2L)
                        .databaseId(2L)
                        .landUseCategoryId(2L)
                        .reportingTableId(2L)
                        .reportingVariableId(2L)
                        .year(1992)
                        .amount(2.0)
                        .unitId(2L)
                        .version(1)
                        .build();

        quantityObservation3 =
                QuantityObservation.builder()
                        .id(3L)
                        .observationTypeId(3L)
                        .taskId(3L)
                        .partyId(3L)
                        .databaseId(3L)
                        .landUseCategoryId(3L)
                        .reportingTableId(3L)
                        .reportingVariableId(3L)
                        .year(1993)
                        .amount(3.0)
                        .unitId(3L)
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
    public void Given_QuantityObservationRecordsExist_When_GetAllWithIdsFilter_Then_OnlyQuantityObservationRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/quantity_observations/all")
                                .queryParam("ids", "{id1}","{id2}")
                                .build(quantityObservation1.getId().toString(), quantityObservation3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
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
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(quantityObservation3.getId());
                            Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(quantityObservation3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getTaskId()).isEqualTo(quantityObservation3.getTaskId());
                            Assertions.assertThat(response.get(1).getPartyId()).isEqualTo(quantityObservation3.getPartyId());
                            Assertions.assertThat(response.get(1).getDatabaseId()).isEqualTo(quantityObservation3.getDatabaseId());
                            Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(quantityObservation3.getReportingTableId());
                            Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(quantityObservation3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getReportingVariableId()).isEqualTo(quantityObservation3.getReportingVariableId());
                            Assertions.assertThat(response.get(1).getYear()).isEqualTo(quantityObservation3.getYear());
                            Assertions.assertThat(response.get(1).getAmount()).isEqualTo(quantityObservation3.getAmount());
                            Assertions.assertThat(response.get(1).getUnitId()).isEqualTo(quantityObservation3.getUnitId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_QuantityObservationRecordsExist_When_GetAllWithTaskIdFilter_Then_OnlyQuantityObservationRecordsWithTheSpecifiedTaskIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/quantity_observations/all")
                                .queryParam("taskId", "{id1}")
                                .build(quantityObservation3.getTaskId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(QuantityObservation.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(quantityObservation3.getId());
                            Assertions.assertThat(response.get(0).getObservationTypeId()).isEqualTo(quantityObservation3.getObservationTypeId());
                            Assertions.assertThat(response.get(0).getTaskId()).isEqualTo(quantityObservation3.getTaskId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(quantityObservation3.getPartyId());
                            Assertions.assertThat(response.get(0).getDatabaseId()).isEqualTo(quantityObservation3.getDatabaseId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(quantityObservation3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(quantityObservation3.getReportingTableId());
                            Assertions.assertThat(response.get(0).getReportingVariableId()).isEqualTo(quantityObservation3.getReportingVariableId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(quantityObservation3.getYear());
                            Assertions.assertThat(response.get(0).getAmount()).isEqualTo(quantityObservation3.getAmount());
                            Assertions.assertThat(response.get(0).getUnitId()).isEqualTo(quantityObservation3.getUnitId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_QuantityObservationRecordsExist_When_GetAllWithPartiesIdsFilter_Then_OnlyQuantityObservationRecordsWithTheSpecifiedPartiesIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/quantity_observations/all")
                                .queryParam("partiesIds", "{id1},{id2}")
                                .build(quantityObservation1.getPartyId().toString(), quantityObservation2.getPartyId().toString()))
                .exchange()
                .expectStatus().isOk()
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
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

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
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_QuantityObservationRecordsExist_When_GetAllWithReportingVariableIdFilter_Then_OnlyQuantityObservationRecordsWithTheSpecifiedReportingVariableIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/quantity_observations/all")
                                .queryParam("reportingVariableId", "{id1}")
                                .build(quantityObservation3.getReportingVariableId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(QuantityObservation.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(quantityObservation3.getId());
                            Assertions.assertThat(response.get(0).getObservationTypeId()).isEqualTo(quantityObservation3.getObservationTypeId());
                            Assertions.assertThat(response.get(0).getTaskId()).isEqualTo(quantityObservation3.getTaskId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(quantityObservation3.getPartyId());
                            Assertions.assertThat(response.get(0).getDatabaseId()).isEqualTo(quantityObservation3.getDatabaseId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(quantityObservation3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(quantityObservation3.getReportingTableId());
                            Assertions.assertThat(response.get(0).getReportingVariableId()).isEqualTo(quantityObservation3.getReportingVariableId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(quantityObservation3.getYear());
                            Assertions.assertThat(response.get(0).getAmount()).isEqualTo(quantityObservation3.getAmount());
                            Assertions.assertThat(response.get(0).getUnitId()).isEqualTo(quantityObservation3.getUnitId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_QuantityObservationRecordsExist_When_GetAllWithYearIdFilter_Then_OnlyQuantityObservationRecordsWithTheSpecifiedYearIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/quantity_observations/all")
                                .queryParam("year", "{id1}")
                                .build(quantityObservation3.getYear().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(QuantityObservation.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(quantityObservation3.getId());
                            Assertions.assertThat(response.get(0).getObservationTypeId()).isEqualTo(quantityObservation3.getObservationTypeId());
                            Assertions.assertThat(response.get(0).getTaskId()).isEqualTo(quantityObservation3.getTaskId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(quantityObservation3.getPartyId());
                            Assertions.assertThat(response.get(0).getDatabaseId()).isEqualTo(quantityObservation3.getDatabaseId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(quantityObservation3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(quantityObservation3.getReportingTableId());
                            Assertions.assertThat(response.get(0).getReportingVariableId()).isEqualTo(quantityObservation3.getReportingVariableId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(quantityObservation3.getYear());
                            Assertions.assertThat(response.get(0).getAmount()).isEqualTo(quantityObservation3.getAmount());
                            Assertions.assertThat(response.get(0).getUnitId()).isEqualTo(quantityObservation3.getUnitId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_QuantityObservationRecordsExist_When_GetAllWithoutFilters_Then_AllQuantityObservationRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/quantity_observations/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
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
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

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
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(quantityObservation3.getId());
                            Assertions.assertThat(response.get(2).getObservationTypeId()).isEqualTo(quantityObservation3.getObservationTypeId());
                            Assertions.assertThat(response.get(2).getTaskId()).isEqualTo(quantityObservation3.getTaskId());
                            Assertions.assertThat(response.get(2).getPartyId()).isEqualTo(quantityObservation3.getPartyId());
                            Assertions.assertThat(response.get(2).getDatabaseId()).isEqualTo(quantityObservation3.getDatabaseId());
                            Assertions.assertThat(response.get(2).getLandUseCategoryId()).isEqualTo(quantityObservation3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(2).getReportingTableId()).isEqualTo(quantityObservation3.getReportingTableId());
                            Assertions.assertThat(response.get(2).getReportingVariableId()).isEqualTo(quantityObservation3.getReportingVariableId());
                            Assertions.assertThat(response.get(2).getYear()).isEqualTo(quantityObservation3.getYear());
                            Assertions.assertThat(response.get(2).getAmount()).isEqualTo(quantityObservation3.getAmount());
                            Assertions.assertThat(response.get(2).getUnitId()).isEqualTo(quantityObservation3.getUnitId());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);
                        }
                );
    }

}
