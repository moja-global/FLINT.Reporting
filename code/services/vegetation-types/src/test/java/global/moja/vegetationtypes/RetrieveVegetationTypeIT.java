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
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveVegetationTypeIT.Initializer.class)
public class RetrieveVegetationTypeIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final VegetationType vegetationType1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("vegetation_types")
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
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
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
    public void Given_VegetationTypeRecordExists_When_GetWithIdParameter_Then_TheVegetationTypeRecordWithThatIdWillBeReturned() {

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
                                .path("/api/v1/vegetation_types/databases/1/ids/{id}")
                                .build(Long.toString(vegetationType1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VegetationType.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(vegetationType1.getId());
                            Assertions.assertThat(response.getCoverTypeId()).isEqualTo(vegetationType1.getCoverTypeId());
                            Assertions.assertThat(response.getName()).isEqualTo(vegetationType1.getName());
                            Assertions.assertThat(response.getWoodyType()).isEqualTo(vegetationType1.getWoodyType());
                            Assertions.assertThat(response.getNaturalSystem()).isEqualTo(vegetationType1.getNaturalSystem());
                        }
                );
    }
}
