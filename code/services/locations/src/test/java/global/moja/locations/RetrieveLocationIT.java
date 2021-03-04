/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations;

import global.moja.locations.models.Location;
import global.moja.locations.util.builders.DatabaseBuilder;
import global.moja.locations.util.builders.LocationBuilder;
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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveLocationIT.Initializer.class)
public class RetrieveLocationIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;


    static final PostgreSQLContainer postgreSQLContainer;
    static final Location location1;

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
    public void Given_LocationRecordExists_When_GetWithIdParameter_Then_TheLocationRecordWithThatIdWillBeReturned() {

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
                                .path("/api/v1/locations/databases/1/ids/{id}")
                                .build(Long.toString(location1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Location.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(location1.getId());
                            Assertions.assertThat(response.getPartyId()).isEqualTo(location1.getPartyId());
                            Assertions.assertThat(response.getTileId()).isEqualTo(location1.getTileId());
                            Assertions.assertThat(response.getVegetationHistoryId()).isEqualTo(location1.getVegetationHistoryId());
                            Assertions.assertThat(response.getUnitCount()).isEqualTo(location1.getUnitCount());
                            Assertions.assertThat(response.getUnitAreaSum()).isEqualTo(location1.getUnitAreaSum());
                        }
                );
    }
}
