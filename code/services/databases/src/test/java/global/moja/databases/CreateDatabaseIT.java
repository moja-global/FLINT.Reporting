/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases;

import global.moja.databases.models.Database;
import global.moja.databases.util.builders.DatabaseBuilder;
import global.moja.databases.util.endpoints.EndpointsUtil;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateDatabaseIT.Initializer.class)
public class CreateDatabaseIT {

    @Spy
    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final Database database4;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("databases")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        database4 =
                new DatabaseBuilder()
                        .id(null)
                        .label("Fourth Database")
                        .description("Fourth Database Description")
                        .url("jdbc:postgresql://localhost:5432/database4")
                        .startYear(1984)
                        .endYear(2004)
                        .processed(true)
                        .published(true)
                        .archived(true)
                        .version(null)
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void Given_DatabaseDetails_When_Post_Then_DatabaseRecordWillBeCreatedAndReturned() {

        Mockito.doReturn(Mono.empty().then()).when(endpointsUtil).integrateDatabase(any(Long.class));


        webTestClient
                .post()
                .uri("/api/v1/databases")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(database4), Database.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Database.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(4L);
                            Assertions.assertThat(response.getLabel()).isEqualTo(database4.getLabel());
                            Assertions.assertThat(response.getDescription()).isEqualTo(database4.getDescription());
                            Assertions.assertThat(response.getUrl()).isEqualTo(database4.getUrl());
                            Assertions.assertThat(response.getStartYear()).isEqualTo(database4.getStartYear());
                            Assertions.assertThat(response.getEndYear()).isEqualTo(database4.getEndYear());
                            Assertions.assertThat(response.getProcessed()).isEqualTo(database4.getProcessed());
                            Assertions.assertThat(response.getPublished()).isEqualTo(database4.getPublished());
                            Assertions.assertThat(response.getArchived()).isEqualTo(database4.getArchived());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
