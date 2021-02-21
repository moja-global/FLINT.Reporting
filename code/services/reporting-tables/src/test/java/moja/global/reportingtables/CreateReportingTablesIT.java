/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables;

import moja.global.reportingtables.models.ReportingTable;
import moja.global.reportingtables.util.builders.ReportingTableBuilder;
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
@ContextConfiguration(initializers = CreateReportingTablesIT.Initializer.class)
public class CreateReportingTablesIT {

    @Autowired
    WebTestClient webTestClient;


    static final PostgreSQLContainer postgreSQLContainer;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("units")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();
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
    public void Given_ReportingTableDetailsList_When_PostAll_Then_ReportingTableRecordsWillBeCreatedAndReturned() {

        ReportingTable u1 =
                new ReportingTableBuilder()
                        .number("Table 4.B")
                        .name("Table 4.B Name")
                        .description("Table 4.B Description")
                        .build();

        ReportingTable u2 =
                new ReportingTableBuilder()
                        .number("Table 4.C")
                        .name("Table 4.C Name")
                        .description("Table 4.C Description")
                        .build();

        ReportingTable[] reportingTables = new ReportingTable[]{u1, u2};

        webTestClient
                .post()
                .uri("/api/v1/reporting_tables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reportingTables), ReportingTable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(ReportingTable.class)
                .value(response -> {

                    Assertions.assertThat(response.get(0).getId() == 4L ||
                            response.get(0).getId() == 5L)
                            .isTrue();

                    Assertions.assertThat(response.get(0).getNumber().equals(u1.getNumber()) ||
                            response.get(0).getNumber().equals(u2.getNumber()))
                            .isTrue();

                    Assertions.assertThat(response.get(0).getVersion() == 1);

                    if (response.get(0).getName().equals(u1.getNumber())) {
                        Assertions.assertThat(response.get(0).getName())
                                .isEqualTo(u1.getName());
                        Assertions.assertThat(response.get(0).getDescription())
                                .isEqualTo(u1.getDescription());
                    } else if (response.get(0).getName().equals(u2.getNumber())) {
                        Assertions.assertThat(response.get(0).getName())
                                .isEqualTo(u2.getName());
                        Assertions.assertThat(response.get(0).getDescription())
                                .isEqualTo(u2.getDescription());
                    }

                    Assertions.assertThat(response.get(1).getId() == 4L ||
                            response.get(1).getId() == 5L)
                            .isTrue();

                    Assertions.assertThat(
                            response.get(1).getNumber().equals(u1.getNumber()) ||
                                    response.get(1).getNumber().equals(u2.getNumber()))
                            .isTrue();

                    Assertions.assertThat(response.get(1).getVersion() == 1);


                    if (response.get(1).getName().equals(u1.getNumber())) {
                        Assertions.assertThat(response.get(1).getName())
                                .isEqualTo(u1.getName());
                        Assertions.assertThat(response.get(1).getDescription())
                                .isEqualTo(u1.getDescription());
                    } else if (response.get(1).getName().equals(u2.getNumber())) {
                        Assertions.assertThat(response.get(1).getName())
                                .isEqualTo(u2.getName());
                        Assertions.assertThat(response.get(1).getDescription())
                                .isEqualTo(u2.getDescription());
                    }

                });
    }
}
