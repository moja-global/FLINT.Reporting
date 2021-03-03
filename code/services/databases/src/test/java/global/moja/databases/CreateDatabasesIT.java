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
@ContextConfiguration(initializers = CreateDatabasesIT.Initializer.class)
public class CreateDatabasesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Database database4;
    static final Database database5;

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

        database5 =
                new DatabaseBuilder()
                        .id(null)
                        .label("Fifth Database")
                        .description("Fifth Database Description")
                        .url("jdbc:postgresql://localhost:5432/database5")
                        .startYear(1985)
                        .endYear(2005)
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

    @Test
    public void Given_DatabaseDetailsList_When_PostAll_Then_DatabaseRecordsWillBeCreatedAndReturned() {

        Database[] databases= new Database[]{database4, database5};

        webTestClient
                .post()
                .uri("/api/v1/databases/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(databases), Database.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Database.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database4.getLabel());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database4.getDescription());
                    Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database4.getUrl());
                    Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database4.getStartYear());
                    Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database4.getEndYear());
                    Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database4.getProcessed());
                    Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database4.getPublished());
                    Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database4.getArchived());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getLabel()).isEqualTo(database5.getLabel());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(database5.getDescription());
                    Assertions.assertThat(response.get(1).getUrl()).isEqualTo(database5.getUrl());
                    Assertions.assertThat(response.get(1).getStartYear()).isEqualTo(database5.getStartYear());
                    Assertions.assertThat(response.get(1).getEndYear()).isEqualTo(database5.getEndYear());
                    Assertions.assertThat(response.get(1).getProcessed()).isEqualTo(database5.getProcessed());
                    Assertions.assertThat(response.get(1).getPublished()).isEqualTo(database5.getPublished());
                    Assertions.assertThat(response.get(1).getArchived()).isEqualTo(database5.getArchived());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
