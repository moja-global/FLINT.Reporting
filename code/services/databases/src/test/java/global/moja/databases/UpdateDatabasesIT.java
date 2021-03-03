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
@ContextConfiguration(initializers = UpdateDatabasesIT.Initializer.class)
public class UpdateDatabasesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Database database1;
    static final Database database2;

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
                        .startYear(1981)
                        .endYear(2001)
                        .processed(true)
                        .published(true)
                        .archived(true)
                        .version(1)
                        .build();

        database2 =
                new DatabaseBuilder()
                        .id(2L)
                        .label("DATABASE 20")
                        .description("DATABASE 20 DESCRIPTION")
                        .url("jdbc:postgresql://localhost:5432/database20")
                        .startYear(1982)
                        .endYear(2002)
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
    public void Given_ModifiedDetailsOfExistingRecords_When_PutAll_Then_TheRecordsWillBeUpdatedAndReturnedWithTheirVersionsIncrementedByOne() {



        Database[] databases= new Database[]{database1, database2};

        webTestClient
                .put()
                .uri("/api/v1/databases/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(databases), Database.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(database1.getId());
                    Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database1.getLabel());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database1.getDescription());
                    Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database1.getUrl());
                    Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database1.getStartYear());
                    Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database1.getEndYear());
                    Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database1.getProcessed());
                    Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database1.getPublished());
                    Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database1.getArchived());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(database2.getId());
                    Assertions.assertThat(response.get(1).getLabel()).isEqualTo(database2.getLabel());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(database2.getDescription());
                    Assertions.assertThat(response.get(1).getUrl()).isEqualTo(database2.getUrl());
                    Assertions.assertThat(response.get(1).getStartYear()).isEqualTo(database2.getStartYear());
                    Assertions.assertThat(response.get(1).getEndYear()).isEqualTo(database2.getEndYear());
                    Assertions.assertThat(response.get(1).getProcessed()).isEqualTo(database2.getProcessed());
                    Assertions.assertThat(response.get(1).getPublished()).isEqualTo(database2.getPublished());
                    Assertions.assertThat(response.get(1).getArchived()).isEqualTo(database2.getArchived());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(database2.getVersion() + 1);


                        }
                );
    }
}
