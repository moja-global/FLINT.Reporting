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
@ContextConfiguration(initializers = UpdateTasksIT.Initializer.class)
public class UpdateTasksIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Task task1;
    static final Task task2;

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
                        .taskTypeId(10L)
                        .taskStatusId(10L)
                        .databaseId(10L)
                        .issues(10000)
                        .resolved(1000)
                        .rejected(100)
                        .note("Note 10")
                        .lastUpdated(1623348872L)
                        .build();
        task2 =
                Task.builder()
                        .id(2L)
                        .taskTypeId(20L)
                        .taskStatusId(20L)
                        .databaseId(20L)
                        .issues(20000)
                        .resolved(2000)
                        .rejected(200)
                        .note("Note 20")
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
    public void Given_ModifiedDetailsOfExistingRecords_When_PutAll_Then_TheRecordsWillBeUpdatedAndReturnedWithTheirVersionsIncrementedByOne() {


        Task[] tasks = new Task[]{task1, task2};

        webTestClient
                .put()
                .uri("/api/v1/tasks/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tasks), Task.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Task.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0)).isEqualTo(task1);
                            Assertions.assertThat(response.get(1)).isEqualTo(task2);


                        }
                );
    }
}
