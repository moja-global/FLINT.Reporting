/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks;

import global.moja.tasks.models.Task;
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
@ContextConfiguration(initializers = RetrieveTasksIT.Initializer.class)
public class RetrieveTasksIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Task task1;
    static final Task task2;
    static final Task task3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("tasks")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        task1 =
                Task.builder()
                        .id(1L)
                        .taskTypeId(1L)
                        .taskStatusId(1L)
                        .databaseId(1L)
                        .issues(1000)
                        .resolved(100)
                        .rejected(10)
                        .note("Note 1")
                        .lastUpdated(1623348871L)
                        .build();
        task2 =
                Task.builder()
                        .id(2L)
                        .taskTypeId(2L)
                        .taskStatusId(2L)
                        .databaseId(2L)
                        .issues(2000)
                        .resolved(200)
                        .rejected(20)
                        .note("Note 2")
                        .lastUpdated(1623348872L)
                        .build();

        task3 =
                Task.builder()
                        .id(3L)
                        .taskTypeId(3L)
                        .taskStatusId(3L)
                        .databaseId(3L)
                        .issues(3000)
                        .resolved(300)
                        .rejected(30)
                        .note("Note 3")
                        .lastUpdated(1623348873L)
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
    public void Given_TaskRecordsExist_When_GetAllWithIdsFilter_Then_OnlyTaskRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/tasks/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(task1.getId().toString(), task3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0)).isEqualTo(task1);
                            Assertions.assertThat(response.get(1)).isEqualTo(task3);


                        }
                );
    }


    @Test
    public void Given_TaskRecordsExist_When_GetAllWithDatabaseIdFilter_Then_OnlyTaskRecordsWithTheSpecifiedDatabaseIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/tasks/all")
                                .queryParam("databaseId", "{id1}")
                                .build(task3.getDatabaseId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0)).isEqualTo(task3);

                        }
                );
    }





    @Test
    public void Given_TaskRecordsExist_When_GetAllWithTaskTaskStatusIdFilter_Then_OnlyTaskRecordsWithTheSpecifiedTaskStatusIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/tasks/all")
                                .queryParam("taskStatusId", "{id1}")
                                .build(task3.getTaskStatusId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0)).isEqualTo(task3);

                        }
                );
    }

    @Test
    public void Given_TaskRecordsExist_When_GetAllWithoutFilters_Then_AllTaskRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/tasks/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0)).isEqualTo(task1);
                            Assertions.assertThat(response.get(1)).isEqualTo(task2);
                            Assertions.assertThat(response.get(2)).isEqualTo(task3);

                        }
                );
    }

}
