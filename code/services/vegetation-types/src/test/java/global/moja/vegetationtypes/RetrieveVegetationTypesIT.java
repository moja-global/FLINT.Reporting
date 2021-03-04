/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes;

import global.moja.vegetationtypes.models.VegetationType;
import global.moja.vegetationtypes.util.builders.DatabaseBuilder;
import global.moja.vegetationtypes.util.builders.VegetationTypeBuilder;
import global.moja.vegetationtypes.util.endpoints.EndpointsUtil;
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
@ContextConfiguration(initializers = RetrieveVegetationTypesIT.Initializer.class)
public class RetrieveVegetationTypesIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final VegetationType vegetationType1;
    static final VegetationType vegetationType2;
    static final VegetationType vegetationType3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("vegetationTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        vegetationType1 =
                new VegetationTypeBuilder()
                        .id(1L)
                        .coverTypeId(1L)
                        .name("First Vegetation Type")
                        .woodyType(false)
                        .naturalSystem(false)
                        .build();

        vegetationType2 =
                new VegetationTypeBuilder()
                        .id(2L)
                        .coverTypeId(2L)
                        .name("Second Vegetation Type")
                        .woodyType(true)
                        .naturalSystem(false)
                        .build();

        vegetationType3 =
                new VegetationTypeBuilder()
                        .id(3L)
                        .coverTypeId(3L)
                        .name("Third Vegetation Type")
                        .woodyType(true)
                        .naturalSystem(true)
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
    public void Given_VegetationTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyVegetationTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(vegetationType1.getId().toString(), vegetationType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationType1.getId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(vegetationType1.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(vegetationType1.getName());
                            Assertions.assertThat(response.get(0).getWoodyType()).isEqualTo(vegetationType1.getWoodyType());
                            Assertions.assertThat(response.get(0).getNaturalSystem()).isEqualTo(vegetationType1.getNaturalSystem());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(vegetationType3.getId());
                            Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(vegetationType3.getCoverTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(vegetationType3.getName());
                            Assertions.assertThat(response.get(1).getWoodyType()).isEqualTo(vegetationType3.getWoodyType());
                            Assertions.assertThat(response.get(1).getNaturalSystem()).isEqualTo(vegetationType3.getNaturalSystem());


                        }
                );
    }


    @Test
    public void Given_VegetationTypeRecordsExist_When_GetAllWithCoverTypeIdFilter_Then_OnlyVegetationTypeRecordsWithTheSpecifiedCoverTypeIdWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/all")
                                .queryParam("coverTypeId", "{id1}")
                                .build(vegetationType3.getCoverTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationType3.getId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(vegetationType3.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(vegetationType3.getName());
                            Assertions.assertThat(response.get(0).getWoodyType()).isEqualTo(vegetationType3.getWoodyType());
                            Assertions.assertThat(response.get(0).getNaturalSystem()).isEqualTo(vegetationType3.getNaturalSystem());

                        }
                );
    }


    @Test
    public void Given_VegetationTypeRecordsExist_When_GetAllWithWoodyTypeFilter_Then_OnlyVegetationTypeRecordsWithTheSpecifiedWoodyTypeWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/all")
                                .queryParam("woodyType", "{id1}")
                                .build(vegetationType3.getWoodyType().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationType2.getId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(vegetationType2.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(vegetationType2.getName());
                            Assertions.assertThat(response.get(0).getWoodyType()).isEqualTo(vegetationType2.getWoodyType());
                            Assertions.assertThat(response.get(0).getNaturalSystem()).isEqualTo(vegetationType2.getNaturalSystem());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(vegetationType3.getId());
                            Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(vegetationType3.getCoverTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(vegetationType3.getName());
                            Assertions.assertThat(response.get(1).getWoodyType()).isEqualTo(vegetationType3.getWoodyType());
                            Assertions.assertThat(response.get(1).getNaturalSystem()).isEqualTo(vegetationType3.getNaturalSystem());

                        }
                );
    }


    @Test
    public void Given_VegetationTypeRecordsExist_When_GetAllWithNaturalSystemFilter_Then_OnlyVegetationTypeRecordsWithTheSpecifiedNaturalSystemWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/all")
                                .queryParam("naturalSystem", "{id1}")
                                .build(vegetationType3.getNaturalSystem().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationType3.getId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(vegetationType3.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(vegetationType3.getName());
                            Assertions.assertThat(response.get(0).getWoodyType()).isEqualTo(vegetationType3.getWoodyType());
                            Assertions.assertThat(response.get(0).getNaturalSystem()).isEqualTo(vegetationType3.getNaturalSystem());

                        }
                );
    }


    @Test
    public void Given_VegetationTypeRecordsExist_When_GetAllWithNameFilter_Then_OnlyVegetationTypeRecordsWithTheSpecifiedNameWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationType3.getId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(vegetationType3.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(vegetationType3.getName());
                            Assertions.assertThat(response.get(0).getWoodyType()).isEqualTo(vegetationType3.getWoodyType());
                            Assertions.assertThat(response.get(0).getNaturalSystem()).isEqualTo(vegetationType3.getNaturalSystem());
                            

                        }
                );
    }


    @Test
    public void Given_VegetationTypeRecordsExist_When_GetAllWithoutFilters_Then_AllVegetationTypeRecordsWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VegetationType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(vegetationType1.getId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(vegetationType1.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(vegetationType1.getName());
                            Assertions.assertThat(response.get(0).getWoodyType()).isEqualTo(vegetationType1.getWoodyType());
                            Assertions.assertThat(response.get(0).getNaturalSystem()).isEqualTo(vegetationType1.getNaturalSystem());
                            

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(vegetationType2.getId());
                            Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(vegetationType2.getCoverTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(vegetationType2.getName());
                            Assertions.assertThat(response.get(1).getWoodyType()).isEqualTo(vegetationType2.getWoodyType());
                            Assertions.assertThat(response.get(1).getNaturalSystem()).isEqualTo(vegetationType2.getNaturalSystem());
                            

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(vegetationType3.getId());
                            Assertions.assertThat(response.get(2).getCoverTypeId()).isEqualTo(vegetationType3.getCoverTypeId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(vegetationType3.getName());
                            Assertions.assertThat(response.get(2).getWoodyType()).isEqualTo(vegetationType3.getWoodyType());
                            Assertions.assertThat(response.get(2).getNaturalSystem()).isEqualTo(vegetationType3.getNaturalSystem());
                            


                        }
                );
    }

}
