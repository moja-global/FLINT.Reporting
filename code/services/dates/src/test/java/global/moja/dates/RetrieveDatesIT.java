/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates;

import global.moja.dates.models.Date;
import global.moja.dates.util.builders.DatabaseBuilder;
import global.moja.dates.util.builders.DateBuilder;
import global.moja.dates.util.endpoints.EndpointsUtil;
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
@ContextConfiguration(initializers = RetrieveDatesIT.Initializer.class)
public class RetrieveDatesIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Date date1;
    static final Date date2;
    static final Date date3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("dates")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        date1 =
                new DateBuilder()
                        .id(1L)
                        .year(1991)
                        .build();

        date2 =
                new DateBuilder()
                        .id(2L)
                        .year(1992)
                        .build();

        date3 =
                new DateBuilder()
                        .id(3L)
                        .year(1993)
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
    public void Given_DateRecordsExist_When_GetAllWithIdsFilter_Then_OnlyDateRecordsWithTheSpecifiedIdsWillBeReturned() {

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
                                .path("/api/v1/dates/databases/1/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(date1.getId().toString(), date3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Date.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(date1.getId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(date1.getYear());


                            Assertions.assertThat(response.get(1).getId()).isEqualTo(date3.getId());
                            Assertions.assertThat(response.get(1).getYear()).isEqualTo(date3.getYear());


                        }
                );
    }


    @Test
    public void Given_DateRecordsExist_When_GetAllWithYearFilter_Then_OnlyDateRecordsWithTheSpecifiedYearWillBeReturned() {

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
                                .path("/api/v1/dates/databases/1/all")
                                .queryParam("year", "{id1}")
                                .build(date3.getYear().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Date.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(date3.getId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(date3.getYear());

                        }
                );
    }

    @Test
    public void Given_DateRecordsExist_When_GetAllWithoutFilters_Then_AllDateRecordsWillBeReturned() {

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
                                .path("/api/v1/dates/databases/1/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Date.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(date1.getId());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(date1.getYear());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(date2.getId());
                            Assertions.assertThat(response.get(1).getYear()).isEqualTo(date2.getYear());

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(date3.getId());
                            Assertions.assertThat(response.get(2).getYear()).isEqualTo(date3.getYear());


                        }
                );
    }

}
