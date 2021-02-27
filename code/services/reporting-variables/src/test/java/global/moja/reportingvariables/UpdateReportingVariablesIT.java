/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables;

import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.util.builders.ReportingVariableBuilder;
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
@ContextConfiguration(initializers = UpdateReportingVariablesIT.Initializer.class)
public class UpdateReportingVariablesIT {

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
    public void Given_ModifiedDetailsOfExistingRecords_When_PutAll_Then_TheRecordsWillBeUpdatedAndReturnedWithTheirVersionsIncrementedByOne() {

        ReportingVariable u2 =
                new ReportingVariableBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .name("2ND VARIABLE")
                        .description("2ND VARIABLE DESCRIPTION")
                        .version(1)
                        .build();

        ReportingVariable u3 =
                new ReportingVariableBuilder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .name("3RD VARIABLE")
                        .description("3RD VARIABLE DESCRIPTION")
                        .version(1)
                        .build();


        ReportingVariable[] reportingVariables = new ReportingVariable[]{u2, u3};

        webTestClient
                .put()
                .uri("/api/v1/reporting_variables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reportingVariables), ReportingVariable.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ReportingVariable.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(u2.getId());
                            Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(u2.getReportingFrameworkId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(u2.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u2.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(u2.getVersion() + 1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(u3.getId());
                            Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(u3.getReportingFrameworkId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(u3.getName());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(u3.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(u3.getVersion() + 1);


                        }
                );
    }
}
