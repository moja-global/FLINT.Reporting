/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults;

import global.moja.fluxreportingresults.models.FluxReportingResult;
import global.moja.fluxreportingresults.util.builders.DatabaseBuilder;
import global.moja.fluxreportingresults.util.builders.FluxReportingResultBuilder;
import global.moja.fluxreportingresults.util.endpoints.EndpointsUtil;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
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
@ContextConfiguration(initializers = RetrieveFluxReportingResultsIT.Initializer.class)
public class RetrieveFluxReportingResultsIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final FluxReportingResult fluxReportingResult1;
    static final FluxReportingResult fluxReportingResult2;
    static final FluxReportingResult fluxReportingResult3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("fluxReportingResults")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        fluxReportingResult1 =
                new FluxReportingResultBuilder()
                        .dateId(1L)
                        .locationId(1L)
                        .fluxTypeId(1L)
                        .sourcePoolId(1L)
                        .sinkPoolId(1L)
                        .build();

        fluxReportingResult2 =
                new FluxReportingResultBuilder()
                        .dateId(2L)
                        .locationId(2L)
                        .fluxTypeId(2L)
                        .sourcePoolId(2L)
                        .sinkPoolId(2L)
                        .build();

        fluxReportingResult3 =
                new FluxReportingResultBuilder()
                        .dateId(3L)
                        .locationId(3L)
                        .fluxTypeId(3L)
                        .sourcePoolId(3L)
                        .sinkPoolId(3L)
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
    public void Given_FluxReportingResultRecordsExist_When_GetAllWithDateIdFilter_Then_OnlyFluxReportingResultRecordsWithTheSpecifiedDateIdWillBeReturned() {

        Mockito
                .when(endpointsUtil.retrieveDatabaseById(1L))
                .thenReturn(
                        Mono.just(
                                new DatabaseBuilder()
                                        .id(null)
                                        .label("First Database")
                                        .description(null)
                                        .url(postgreSQLContainer.getJdbcUrl())
                                        .startYear(1984)
                                        .endYear(2014)
                                        .processed(false)
                                        .published(false)
                                        .archived(false)
                                        .build()));

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_reporting_results/databases/1/all")
                                .queryParam("dateId", "{id1}")
                                .build(fluxReportingResult3.getDateId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxReportingResult.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getDateId()).isEqualTo(fluxReportingResult3.getDateId());
                            Assertions.assertThat(response.get(0).getLocationId()).isEqualTo(fluxReportingResult3.getLocationId());
                            Assertions.assertThat(response.get(0).getSourcePoolId()).isEqualTo(fluxReportingResult3.getSourcePoolId());
                            Assertions.assertThat(response.get(0).getSinkPoolId()).isEqualTo(fluxReportingResult3.getSinkPoolId());

                        }
                );
    }


    @Test
    public void Given_FluxReportingResultRecordsExist_When_GetAllWithLocationIdFilter_Then_OnlyFluxReportingResultRecordsWithTheSpecifiedLocationIdWillBeReturned() {

        Mockito
                .when(endpointsUtil.retrieveDatabaseById(1L))
                .thenReturn(
                        Mono.just(
                                new DatabaseBuilder()
                                        .id(null)
                                        .label("First Database")
                                        .description(null)
                                        .url(postgreSQLContainer.getJdbcUrl())
                                        .startYear(1984)
                                        .endYear(2014)
                                        .processed(false)
                                        .published(false)
                                        .archived(false)
                                        .build()));

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_reporting_results/databases/1/all")
                                .queryParam("locationId", "{id1}")
                                .build(fluxReportingResult3.getLocationId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxReportingResult.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getDateId()).isEqualTo(fluxReportingResult3.getDateId());
                            Assertions.assertThat(response.get(0).getLocationId()).isEqualTo(fluxReportingResult3.getLocationId());
                            Assertions.assertThat(response.get(0).getSourcePoolId()).isEqualTo(fluxReportingResult3.getSourcePoolId());
                            Assertions.assertThat(response.get(0).getSinkPoolId()).isEqualTo(fluxReportingResult3.getSinkPoolId());

                        }
                );
    }


    @Test
    public void Given_FluxReportingResultRecordsExist_When_GetAllWithSourcePoolIdFilter_Then_OnlyFluxReportingResultRecordsWithTheSpecifiedSourcePoolWillBeReturned() {

        Mockito
                .when(endpointsUtil.retrieveDatabaseById(1L))
                .thenReturn(
                        Mono.just(
                                new DatabaseBuilder()
                                        .id(null)
                                        .label("First Database")
                                        .description(null)
                                        .url(postgreSQLContainer.getJdbcUrl())
                                        .startYear(1984)
                                        .endYear(2014)
                                        .processed(false)
                                        .published(false)
                                        .archived(false)
                                        .build()));

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_reporting_results/databases/1/all")
                                .queryParam("sourcePoolId", "{id1}")
                                .build(fluxReportingResult3.getSourcePoolId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxReportingResult.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getDateId()).isEqualTo(fluxReportingResult3.getDateId());
                            Assertions.assertThat(response.get(0).getLocationId()).isEqualTo(fluxReportingResult3.getLocationId());
                            Assertions.assertThat(response.get(0).getSourcePoolId()).isEqualTo(fluxReportingResult3.getSourcePoolId());
                            Assertions.assertThat(response.get(0).getSinkPoolId()).isEqualTo(fluxReportingResult3.getSinkPoolId());

                        }
                );
    }

    @Test
    public void Given_FluxReportingResultRecordsExist_When_GetAllWithSinkPoolIdFilter_Then_OnlyFluxReportingResultRecordsWithTheSpecifiedSinkPoolWillBeReturned() {

        Mockito
                .when(endpointsUtil.retrieveDatabaseById(1L))
                .thenReturn(
                        Mono.just(
                                new DatabaseBuilder()
                                        .id(null)
                                        .label("First Database")
                                        .description(null)
                                        .url(postgreSQLContainer.getJdbcUrl())
                                        .startYear(1984)
                                        .endYear(2014)
                                        .processed(false)
                                        .published(false)
                                        .archived(false)
                                        .build()));

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_reporting_results/databases/1/all")
                                .queryParam("sinkPoolId", "{id1}")
                                .build(fluxReportingResult3.getSinkPoolId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxReportingResult.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getDateId()).isEqualTo(fluxReportingResult3.getDateId());
                            Assertions.assertThat(response.get(0).getLocationId()).isEqualTo(fluxReportingResult3.getLocationId());
                            Assertions.assertThat(response.get(0).getSourcePoolId()).isEqualTo(fluxReportingResult3.getSourcePoolId());
                            Assertions.assertThat(response.get(0).getSinkPoolId()).isEqualTo(fluxReportingResult3.getSinkPoolId());

                        }
                );
    }

    @Test
    public void Given_FluxReportingResultRecordsExist_When_GetAllWithoutFilters_Then_AllFluxReportingResultRecordsWillBeReturned() {

        Mockito
                .when(endpointsUtil.retrieveDatabaseById(1L))
                .thenReturn(
                        Mono.just(
                                new DatabaseBuilder()
                                        .id(null)
                                        .label("First Database")
                                        .description(null)
                                        .url(postgreSQLContainer.getJdbcUrl())
                                        .startYear(1984)
                                        .endYear(2014)
                                        .processed(false)
                                        .published(false)
                                        .archived(false)
                                        .build()));

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_reporting_results/databases/1/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxReportingResult.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getDateId()).isEqualTo(fluxReportingResult1.getDateId());
                            Assertions.assertThat(response.get(0).getLocationId()).isEqualTo(fluxReportingResult1.getLocationId());
                            Assertions.assertThat(response.get(0).getSourcePoolId()).isEqualTo(fluxReportingResult1.getSourcePoolId());
                            Assertions.assertThat(response.get(0).getSinkPoolId()).isEqualTo(fluxReportingResult1.getSinkPoolId());


                            Assertions.assertThat(response.get(1).getDateId()).isEqualTo(fluxReportingResult2.getDateId());
                            Assertions.assertThat(response.get(1).getLocationId()).isEqualTo(fluxReportingResult2.getLocationId());
                            Assertions.assertThat(response.get(1).getSourcePoolId()).isEqualTo(fluxReportingResult2.getSourcePoolId());
                            Assertions.assertThat(response.get(1).getSinkPoolId()).isEqualTo(fluxReportingResult2.getSinkPoolId());


                            Assertions.assertThat(response.get(2).getDateId()).isEqualTo(fluxReportingResult3.getDateId());
                            Assertions.assertThat(response.get(2).getLocationId()).isEqualTo(fluxReportingResult3.getLocationId());
                            Assertions.assertThat(response.get(2).getSourcePoolId()).isEqualTo(fluxReportingResult3.getSourcePoolId());
                            Assertions.assertThat(response.get(2).getSinkPoolId()).isEqualTo(fluxReportingResult3.getSinkPoolId());


                        }
                );
    }

}
