/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations;

import global.moja.locations.util.builders.DatabaseBuilder;
import global.moja.locations.util.builders.LocationBuilder;
import global.moja.locations.models.Location;
import global.moja.locations.util.endpoints.EndpointsUtil;
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
@ContextConfiguration(initializers = RetrieveLocationsIT.Initializer.class)
public class RetrieveLocationsIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Location location1;
    static final Location location2;
    static final Location location3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("locations")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        location1 =
                new LocationBuilder()
                        .id(1L)
                        .partyId(1L)
                        .tileId(1L)
                        .vegetationHistoryId(1L)
                        .unitCount(1L)
                        .unitAreaSum(1.0)
                        .build();

        location2 =
                new LocationBuilder()
                        .id(2L)
                        .partyId(2L)
                        .tileId(2L)
                        .vegetationHistoryId(2L)
                        .unitCount(2L)
                        .unitAreaSum(2.0)
                        .build();

        location3 =
                new LocationBuilder()
                        .id(3L)
                        .partyId(3L)
                        .tileId(3L)
                        .vegetationHistoryId(3L)
                        .unitCount(3L)
                        .unitAreaSum(3.0)
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
    public void Given_LocationRecordsExist_When_GetAllWithIdsFilter_Then_OnlyLocationRecordsWithTheSpecifiedIdsWillBeReturned() {

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
                                .path("/api/v1/locations/databases/1/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(location1.getId().toString(), location3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Location.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(location1.getId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(location1.getPartyId());
                            Assertions.assertThat(response.get(0).getTileId()).isEqualTo(location1.getTileId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(location1.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getUnitCount()).isEqualTo(location1.getUnitCount());
                            Assertions.assertThat(response.get(0).getUnitAreaSum()).isEqualTo(location1.getUnitAreaSum());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(location3.getId());
                            Assertions.assertThat(response.get(1).getPartyId()).isEqualTo(location3.getPartyId());
                            Assertions.assertThat(response.get(1).getTileId()).isEqualTo(location3.getTileId());
                            Assertions.assertThat(response.get(1).getVegetationHistoryId()).isEqualTo(location3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(1).getUnitCount()).isEqualTo(location3.getUnitCount());
                            Assertions.assertThat(response.get(1).getUnitAreaSum()).isEqualTo(location3.getUnitAreaSum());


                        }
                );
    }


    @Test
    public void Given_LocationRecordsExist_When_GetAllWithPartyIdFilter_Then_OnlyLocationRecordsWithTheSpecifiedPartyIdWillBeReturned() {

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
                                .path("/api/v1/locations/databases/1/all")
                                .queryParam("partyId", "{id1}")
                                .build(location3.getPartyId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Location.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(location3.getId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(location3.getPartyId());
                            Assertions.assertThat(response.get(0).getTileId()).isEqualTo(location3.getTileId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(location3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getUnitCount()).isEqualTo(location3.getUnitCount());
                            Assertions.assertThat(response.get(0).getUnitAreaSum()).isEqualTo(location3.getUnitAreaSum());

                        }
                );
    }


    @Test
    public void Given_LocationRecordsExist_When_GetAllWithTileIdFilter_Then_OnlyLocationRecordsWithTheSpecifiedTileIdWillBeReturned() {

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
                                .path("/api/v1/locations/databases/1/all")
                                .queryParam("tileId", "{id1}")
                                .build(location3.getTileId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Location.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(location3.getId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(location3.getPartyId());
                            Assertions.assertThat(response.get(0).getTileId()).isEqualTo(location3.getTileId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(location3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getUnitCount()).isEqualTo(location3.getUnitCount());
                            Assertions.assertThat(response.get(0).getUnitAreaSum()).isEqualTo(location3.getUnitAreaSum());

                        }
                );
    }


    @Test
    public void Given_LocationRecordsExist_When_GetAllWithLocationVegetationHistoryIdFilter_Then_OnlyLocationRecordsWithTheSpecifiedVegetationHistoryIdWillBeReturned() {

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
                                .path("/api/v1/locations/databases/1/all")
                                .queryParam("vegetationHistoryId", "{id1}")
                                .build(location3.getVegetationHistoryId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Location.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(location3.getId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(location3.getPartyId());
                            Assertions.assertThat(response.get(0).getTileId()).isEqualTo(location3.getTileId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(location3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getUnitCount()).isEqualTo(location3.getUnitCount());
                            Assertions.assertThat(response.get(0).getUnitAreaSum()).isEqualTo(location3.getUnitAreaSum());

                        }
                );
    }

    @Test
    public void Given_LocationRecordsExist_When_GetAllWithoutFilters_Then_AllLocationRecordsWillBeReturned() {

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
                                .path("/api/v1/locations/databases/1/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Location.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(location1.getId());
                            Assertions.assertThat(response.get(0).getPartyId()).isEqualTo(location1.getPartyId());
                            Assertions.assertThat(response.get(0).getTileId()).isEqualTo(location1.getTileId());
                            Assertions.assertThat(response.get(0).getVegetationHistoryId()).isEqualTo(location1.getVegetationHistoryId());
                            Assertions.assertThat(response.get(0).getUnitCount()).isEqualTo(location1.getUnitCount());
                            Assertions.assertThat(response.get(0).getUnitAreaSum()).isEqualTo(location1.getUnitAreaSum());;

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(location2.getId());
                            Assertions.assertThat(response.get(1).getPartyId()).isEqualTo(location2.getPartyId());
                            Assertions.assertThat(response.get(1).getTileId()).isEqualTo(location2.getTileId());
                            Assertions.assertThat(response.get(1).getVegetationHistoryId()).isEqualTo(location2.getVegetationHistoryId());
                            Assertions.assertThat(response.get(1).getUnitCount()).isEqualTo(location2.getUnitCount());
                            Assertions.assertThat(response.get(1).getUnitAreaSum()).isEqualTo(location2.getUnitAreaSum());

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(location3.getId());
                            Assertions.assertThat(response.get(2).getPartyId()).isEqualTo(location3.getPartyId());
                            Assertions.assertThat(response.get(2).getTileId()).isEqualTo(location3.getTileId());
                            Assertions.assertThat(response.get(2).getVegetationHistoryId()).isEqualTo(location3.getVegetationHistoryId());
                            Assertions.assertThat(response.get(2).getUnitCount()).isEqualTo(location3.getUnitCount());
                            Assertions.assertThat(response.get(2).getUnitAreaSum()).isEqualTo(location3.getUnitAreaSum());


                        }
                );
    }

}
