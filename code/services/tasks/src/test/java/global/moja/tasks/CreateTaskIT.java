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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateTaskIT.Initializer.class)
public class CreateTaskIT {

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final Task task4;

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
                        .id(null)
                        .taskTypeId(4L)
                        .taskStatusId(4L)
                        .databaseId(4L)
                        .issues(4000)
                        .resolved(400)
                        .rejected(40)
                        .note("Note 4")
                        .lastUpdated(null)
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
    public void Given_TaskDetails_When_Post_Then_TaskRecordWillBeCreatedAndReturned() {

        Long currentTime = System.currentTimeMillis();

        webTestClient
                .post()
                .uri("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(task4), Task.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Task.class)
                .value(response -> {

                    Assertions.assertThat(response.getId()).isEqualTo(4L);
                    Assertions.assertThat(response.getTaskTypeId()).isEqualTo(task4.getTaskTypeId());
                    Assertions.assertThat(response.getTaskStatusId()).isEqualTo(task4.getTaskStatusId());
                    Assertions.assertThat(response.getDatabaseId()).isEqualTo(task4.getDatabaseId());
                    Assertions.assertThat(response.getIssues()).isEqualTo(task4.getIssues());
                    Assertions.assertThat(response.getResolved()).isEqualTo(task4.getResolved());
                    Assertions.assertThat(response.getRejected()).isEqualTo(task4.getRejected());
                    Assertions.assertThat(response.getNote()).isEqualTo(task4.getNote());
                    Assertions.assertThat(response.getLastUpdated()).isGreaterThan(currentTime);

                        }
                );
    }
}
