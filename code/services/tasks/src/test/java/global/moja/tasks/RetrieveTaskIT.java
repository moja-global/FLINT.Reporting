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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveTaskIT.Initializer.class)
public class RetrieveTaskIT {

    @Autowired
    WebTestClient webTestClient;


    static final PostgreSQLContainer postgreSQLContainer;
    static final Task task1;

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
    public void Given_TaskRecordExists_When_GetWithIdParameter_Then_TheTaskRecordWithThatIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/tasks/ids/{id}")
                                .build(Long.toString(task1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Task.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(task1.getId());
                            Assertions.assertThat(response.getTaskTypeId()).isEqualTo(task1.getTaskTypeId());
                            Assertions.assertThat(response.getTaskStatusId()).isEqualTo(task1.getTaskStatusId());
                            Assertions.assertThat(response.getDatabaseId()).isEqualTo(task1.getDatabaseId());
                            Assertions.assertThat(response.getIssues()).isEqualTo(task1.getIssues());
                            Assertions.assertThat(response.getResolved()).isEqualTo(task1.getResolved());
                            Assertions.assertThat(response.getRejected()).isEqualTo(task1.getRejected());
                            Assertions.assertThat(response.getNote()).isEqualTo(task1.getNote());
                            Assertions.assertThat(response.getLastUpdated()).isNotNull();
                        }
                );
    }
}
