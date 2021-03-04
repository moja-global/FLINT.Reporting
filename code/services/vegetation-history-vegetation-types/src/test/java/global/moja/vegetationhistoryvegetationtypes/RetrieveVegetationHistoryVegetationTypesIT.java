/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes;

import global.moja.vegetationhistoryvegetationtypes.util.builders.DatabaseBuilder;
import global.moja.vegetationhistoryvegetationtypes.util.builders.VegetationHistoryVegetationTypeBuilder;
import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;
import global.moja.vegetationhistoryvegetationtypes.util.endpoints.EndpointsUtil;
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
@ContextConfiguration(initializers = RetrieveVegetationHistoryVegetationTypesIT.Initializer.class)
public class RetrieveVegetationHistoryVegetationTypesIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final VegetationHistoryVegetationType vegetationHistoryVegetationType1;
    static final VegetationHistoryVegetationType vegetationHistoryVegetationType2;
    static final VegetationHistoryVegetationType vegetationHistoryVegetationType3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("vegetationHistoryVegetationTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        vegetationHistoryVegetationType1 =
                new VegetationHistoryVegetationTypeBuilder()
                        .id(1L)
                        .vegetationHistoryId(1L)
                        .vegetationTypeId(1L)
                        .itemNumber(1L)
                        .year(1991)
                        .build();

        vegetationHistoryVegetationType2 =
                new VegetationHistoryVegetationTypeBuilder()
                        .id(2L)
                        .vegetationHistoryId(2L)
                        .vegetationTypeId(2L)
                        .itemNumber(2L)
                        .year(1992)
                        .build();

        vegetationHistoryVegetationType3 =
                new VegetationHistoryVegetationTypeBuilder()
                        .id(3L)
                        .vegetationHistoryId(3L)
                        .vegetationTypeId(3L)
                        .itemNumber(3L)
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
    public void Given_VegetationHistoryVegetationTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyVegetationHistoryVegetationTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

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
                                .path("/api/v1/vegetation_history_vegetation_types/databases/1/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(vegetationHistoryVegetationType1.getId().toString(), vegetationHistoryVegetationType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationHistoryVegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationHistoryVegetationType1.getId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType1.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType1.getVegetationTypeId());
                            Assertions.assertThat(response.get(0).getItemNumber()).isEqualTo(vegetationHistoryVegetationType1.getItemNumber());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(vegetationHistoryVegetationType1.getYear());
                            

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(vegetationHistoryVegetationType3.getId());
                            Assertions.assertThat(response.get(1).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(1).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationTypeId());
                            Assertions.assertThat(response.get(1).getItemNumber()).isEqualTo(vegetationHistoryVegetationType3.getItemNumber());
                            Assertions.assertThat(response.get(1).getYear()).isEqualTo(vegetationHistoryVegetationType3.getYear());
                            


                        }
                );
    }


    @Test
    public void Given_VegetationHistoryVegetationTypeRecordsExist_When_GetAllWithVegetationHistoryIdFilter_Then_OnlyVegetationHistoryVegetationTypeRecordsWithTheSpecifiedVegetationHistoryIdWillBeReturned() {

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
                                .path("/api/v1/vegetation_history_vegetation_types/databases/1/all")
                                .queryParam("vegetationHistoryId", "{id1}")
                                .build(vegetationHistoryVegetationType3.getVegetationHistoryId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationHistoryVegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationHistoryVegetationType3.getId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationTypeId());
                            Assertions.assertThat(response.get(0).getItemNumber()).isEqualTo(vegetationHistoryVegetationType3.getItemNumber());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(vegetationHistoryVegetationType3.getYear());
                            

                        }
                );
    }


    @Test
    public void Given_VegetationHistoryVegetationTypeRecordsExist_When_GetAllWithVegetationTypeIdFilter_Then_OnlyVegetationHistoryVegetationTypeRecordsWithTheSpecifiedVegetationTypeIdWillBeReturned() {

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
                                .path("/api/v1/vegetation_history_vegetation_types/databases/1/all")
                                .queryParam("vegetationTypeId", "{id1}")
                                .build(vegetationHistoryVegetationType3.getVegetationTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationHistoryVegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationHistoryVegetationType3.getId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationTypeId());
                            Assertions.assertThat(response.get(0).getItemNumber()).isEqualTo(vegetationHistoryVegetationType3.getItemNumber());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(vegetationHistoryVegetationType3.getYear());
                            

                        }
                );
    }


    @Test
    public void Given_VegetationHistoryVegetationTypeRecordsExist_When_GetAllWithItemNumberFilter_Then_OnlyVegetationHistoryVegetationTypeRecordsWithTheSpecifiedItemNumberWillBeReturned() {

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
                                .path("/api/v1/vegetation_history_vegetation_types/databases/1/all")
                                .queryParam("itemNumber", "{id1}")
                                .build(vegetationHistoryVegetationType3.getItemNumber().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationHistoryVegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationHistoryVegetationType3.getId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationTypeId());
                            Assertions.assertThat(response.get(0).getItemNumber()).isEqualTo(vegetationHistoryVegetationType3.getItemNumber());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(vegetationHistoryVegetationType3.getYear());
                            

                        }
                );
    }


    @Test
    public void Given_VegetationHistoryVegetationTypeRecordsExist_When_GetAllWithoutFilters_Then_AllVegetationHistoryVegetationTypeRecordsWillBeReturned() {

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
                                .path("/api/v1/vegetation_history_vegetation_types/databases/1/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationHistoryVegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationHistoryVegetationType1.getId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType1.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType1.getVegetationTypeId());
                            Assertions.assertThat(response.get(0).getItemNumber()).isEqualTo(vegetationHistoryVegetationType1.getItemNumber());
                            Assertions.assertThat(response.get(0).getYear()).isEqualTo(vegetationHistoryVegetationType1.getYear());
                            

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(vegetationHistoryVegetationType2.getId());
                            Assertions.assertThat(response.get(1).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType2.getVegetationHistoryId());
                            Assertions.assertThat(response.get(1).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType2.getVegetationTypeId());
                            Assertions.assertThat(response.get(1).getItemNumber()).isEqualTo(vegetationHistoryVegetationType2.getItemNumber());
                            Assertions.assertThat(response.get(1).getYear()).isEqualTo(vegetationHistoryVegetationType2.getYear());
                            

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(vegetationHistoryVegetationType3.getId());
                            Assertions.assertThat(response.get(2).getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(2).getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType3.getVegetationTypeId());
                            Assertions.assertThat(response.get(2).getItemNumber()).isEqualTo(vegetationHistoryVegetationType3.getItemNumber());
                            Assertions.assertThat(response.get(2).getYear()).isEqualTo(vegetationHistoryVegetationType3.getYear());
                            


                        }
                );
    }

}
