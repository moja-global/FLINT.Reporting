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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = UpdateDatabaseIT.Initializer.class)
public class UpdateDatabaseIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Database database1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("databases")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        database1 =
                new DatabaseBuilder()
                        .id(1L)
                        .label("DATABASE 10")
                        .description("DATABASE 10 DESCRIPTION")
                        .url("jdbc:postgresql://localhost:5432/database10")
                        .startYear(1984)
                        .endYear(2004)
                        .processed(true)
                        .published(true)
                        .archived(true)
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
    public void Given_ModifiedDetailsOfAnExistingRecord_When_Put_Then_TheRecordWillBeUpdatedAndReturnedWithItsVersionIncrementedByOne() {

        webTestClient
                .put()
                .uri("/api/v1/databases")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(database1), Database.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Database.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(database1.getId());
                            Assertions.assertThat(response.getLabel()).isEqualTo(database1.getLabel());
                            Assertions.assertThat(response.getDescription()).isEqualTo(database1.getDescription());
                            Assertions.assertThat(response.getUrl()).isEqualTo(database1.getUrl());
                            Assertions.assertThat(response.getStartYear()).isEqualTo(database1.getStartYear());
                            Assertions.assertThat(response.getEndYear()).isEqualTo(database1.getEndYear());
                            Assertions.assertThat(response.getProcessed()).isEqualTo(database1.getProcessed());
                            Assertions.assertThat(response.getPublished()).isEqualTo(database1.getPublished());
                            Assertions.assertThat(response.getArchived()).isEqualTo(database1.getArchived());
                            Assertions.assertThat(response.getVersion()).isEqualTo(database1.getVersion() + 1);
                        }
                );
    }
}
