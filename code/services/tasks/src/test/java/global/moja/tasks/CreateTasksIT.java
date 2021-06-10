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
@ContextConfiguration(initializers = CreateTasksIT.Initializer.class)
public class CreateTasksIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Task task4;
    static final Task task5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("tasks")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        task4 =
                Task.builder()
                        .id(4L)
                        .taskTypeId(4L)
                        .taskStatusId(4L)
                        .databaseId(4L)
                        .issues(4000)
                        .resolved(400)
                        .rejected(40)
                        .note("Note 4")
                        .lastUpdated(1623348874L)
                        .build();

        task5 =
                Task.builder()
                        .id(5L)
                        .taskTypeId(5L)
                        .taskStatusId(5L)
                        .databaseId(5L)
                        .issues(5000)
                        .resolved(500)
                        .rejected(50)
                        .note("Note 5")
                        .lastUpdated(1623348875L)
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
    public void Given_TaskDetailsList_When_PostAll_Then_TaskRecordsWillBeCreatedAndReturned() {

        Task[] tasks= new Task[]{task4, task5};

        webTestClient
                .post()
                .uri("/api/v1/tasks/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tasks), Task.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Task.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0)).isEqualTo(task4);
                    Assertions.assertThat(response.get(1)).isEqualTo(task5);


                });
    }
}
