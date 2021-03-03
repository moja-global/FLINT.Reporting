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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveDatabasesIT.Initializer.class)
public class RetrieveDatabasesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Database database1;
    static final Database database2;
    static final Database database3;

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
                        .label("First Database")
                        .description("First Database Description")
                        .url("jdbc:postgresql://localhost:5432/database1")
                        .startYear(1981)
                        .endYear(2001)
                        .processed(false)
                        .published(false)
                        .archived(false)
                        .version(1)
                        .build();

        database2 =
                new DatabaseBuilder()
                        .id(2L)
                        .label("Second Database")
                        .description("Second Database Description")
                        .url("jdbc:postgresql://localhost:5432/database2")
                        .startYear(1982)
                        .endYear(2002)
                        .processed(true)
                        .published(false)
                        .archived(false)
                        .version(1)
                        .build();

        database3 =
                new DatabaseBuilder()
                        .id(3L)
                        .label("Third Database")
                        .description("Third Database Description")
                        .url("jdbc:postgresql://localhost:5432/database3")
                        .startYear(1983)
                        .endYear(2003)
                        .processed(true)
                        .published(true)
                        .archived(false)
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
    public void Given_DatabaseRecordsExist_When_GetAllWithIdsFilter_Then_OnlyDatabaseRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(database1.getId().toString(), database3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
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
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database1.getVersion());


                            Assertions.assertThat(response.get(1).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(1).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(1).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(1).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(1).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(1).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(1).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(1).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(database3.getVersion());

                        }
                );
    }


    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithLabelFilter_Then_OnlyDatabaseRecordsWithTheSpecifiedLabelWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("label", "{label1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database3.getVersion());

                        }
                );
    }


    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithStartYearFilter_Then_OnlyDatabaseRecordsWithTheSpecifiedStartYearWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("startYear", "{startYear}")
                                .build(database3.getStartYear().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database3.getVersion());

                        }
                );
    }


    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithEndYearFilter_Then_OnlyDatabaseRecordsWithTheSpecifiedEndYearWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("endYear", "{endYear}")
                                .build(database3.getEndYear().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database3.getVersion());

                        }
                );
    }

    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithProcessedFilter_Then_OnlyProcessedDatabaseRecordsWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("processed", "{status}")
                                .build("true"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(database2.getId());
                            Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database2.getLabel());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database2.getDescription());
                            Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database2.getUrl());
                            Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database2.getStartYear());
                            Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database2.getEndYear());
                            Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database2.getProcessed());
                            Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database2.getPublished());
                            Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database2.getArchived());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database2.getVersion());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(1).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(1).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(1).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(1).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(1).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(1).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(1).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(database3.getVersion());

                        }
                );
    }


    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithPublishedFilter_Then_OnlyPublishedDatabaseRecordsWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("published", "{status}")
                                .build("true"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(0).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(0).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(0).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(0).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(0).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(0).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(0).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database3.getVersion());

                        }
                );
    }


    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithArchivedFilter_Then_OnlyArchivedDatabaseRecordsWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .queryParam("archived", "{status}")
                                .build("true"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Database.class)
                .value(response -> {

                            Assertions.assertThat(response).isEmpty();

                        }
                );
    }


    @Test
    public void Given_DatabaseRecordsExist_When_GetAllWithoutFilters_Then_AllDatabaseRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/databases/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
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
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(database1.getVersion());

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(database2.getId());
                            Assertions.assertThat(response.get(1).getLabel()).isEqualTo(database2.getLabel());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(database2.getDescription());
                            Assertions.assertThat(response.get(1).getUrl()).isEqualTo(database2.getUrl());
                            Assertions.assertThat(response.get(1).getStartYear()).isEqualTo(database2.getStartYear());
                            Assertions.assertThat(response.get(1).getEndYear()).isEqualTo(database2.getEndYear());
                            Assertions.assertThat(response.get(1).getProcessed()).isEqualTo(database2.getProcessed());
                            Assertions.assertThat(response.get(1).getPublished()).isEqualTo(database2.getPublished());
                            Assertions.assertThat(response.get(1).getArchived()).isEqualTo(database2.getArchived());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(database2.getVersion());

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(database3.getId());
                            Assertions.assertThat(response.get(2).getLabel()).isEqualTo(database3.getLabel());
                            Assertions.assertThat(response.get(2).getDescription()).isEqualTo(database3.getDescription());
                            Assertions.assertThat(response.get(2).getUrl()).isEqualTo(database3.getUrl());
                            Assertions.assertThat(response.get(2).getStartYear()).isEqualTo(database3.getStartYear());
                            Assertions.assertThat(response.get(2).getEndYear()).isEqualTo(database3.getEndYear());
                            Assertions.assertThat(response.get(2).getProcessed()).isEqualTo(database3.getProcessed());
                            Assertions.assertThat(response.get(2).getPublished()).isEqualTo(database3.getPublished());
                            Assertions.assertThat(response.get(2).getArchived()).isEqualTo(database3.getArchived());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(database3.getVersion());


                        }
                );
    }

}
