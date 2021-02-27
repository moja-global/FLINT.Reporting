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
@ContextConfiguration(initializers = UpdateReportingTablesIT.Initializer.class)
public class UpdateReportingTablesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final ReportingTable reportingTable1;
    static final ReportingTable reportingTable2;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("reportingTables")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        reportingTable1 =
                new ReportingTableBuilder()
                        .id(1L)
                        .reportingFrameworkId(1L)
                        .number("FIRST")
                        .name("FIRST REPORTING TABLE")
                        .description("FIRST REPORTING DESCRIPTION")
                        .version(1)
                        .build();

        reportingTable2 =
                new ReportingTableBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .number("SECOND")
                        .name("SECOND REPORTING TABLE")
                        .description("SECOND REPORTING DESCRIPTION")
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



        ReportingTable[] reportingTables= new ReportingTable[]{reportingTable1, reportingTable2};

        webTestClient
                .put()
                .uri("/api/v1/reporting_tables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reportingTables), ReportingTable.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ReportingTable.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(reportingTable1.getId());
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(reportingTable1.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getNumber()).isEqualTo(reportingTable1.getNumber());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(reportingTable1.getName());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(reportingTable1.getDescription());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(reportingTable1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(reportingTable2.getId());
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(reportingTable2.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getNumber()).isEqualTo(reportingTable2.getNumber());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(reportingTable2.getName());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(reportingTable2.getDescription());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(reportingTable2.getVersion() + 1);


                        }
                );
    }
}
