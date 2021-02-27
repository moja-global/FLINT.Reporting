/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable;

import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.util.builders.ReportingTableBuilder;
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
@ContextConfiguration(initializers = CreateReportingTablesIT.Initializer.class)
public class CreateReportingTablesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final ReportingTable reportingTable4;
    static final ReportingTable reportingTable5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("reportingTables")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        reportingTable4 =
                new ReportingTableBuilder()
                        .id(null)
                        .reportingFrameworkId(1L)
                        .number("Fourth")
                        .name("Fourth ReportingTable")
                        .description("Fourth ReportingTable Description")
                        .version(null)
                        .build();

        reportingTable5 =
                new ReportingTableBuilder()
                        .id(null)
                        .reportingFrameworkId(1L)
                        .number("Fifth")
                        .name("Fifth ReportingTable")
                        .description("Fifth ReportingTable Description")
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
    public void Given_ReportingTableDetailsList_When_PostAll_Then_ReportingTableRecordsWillBeCreatedAndReturned() {

        ReportingTable[] reportingTables= new ReportingTable[]{reportingTable4, reportingTable5};

        webTestClient
                .post()
                .uri("/api/v1/reporting_tables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reportingTables), ReportingTable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(ReportingTable.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(reportingTable4.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getNumber()).isEqualTo(reportingTable4.getNumber());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(reportingTable4.getName());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(reportingTable4.getDescription());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(reportingTable5.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getNumber()).isEqualTo(reportingTable5.getNumber());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(reportingTable5.getName());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(reportingTable5.getDescription());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
