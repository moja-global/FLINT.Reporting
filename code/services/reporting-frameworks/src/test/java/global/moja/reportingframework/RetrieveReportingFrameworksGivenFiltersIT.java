/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework;

import global.moja.reportingframework.util.builders.ReportingFrameworkBuilder;
import global.moja.reportingframework.models.ReportingFramework;
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
@ContextConfiguration(initializers = RetrieveReportingFrameworksGivenFiltersIT.Initializer.class)
public class RetrieveReportingFrameworksGivenFiltersIT {

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
    public void Given_ReportingFrameworkRecordsExist_When_GetAllWithIdsFilter_Then_OnlyReportingFrameworkRecordsWithTheSpecifiedIdsWillBeReturned() {

        ReportingFramework u1 =
                new ReportingFrameworkBuilder()
                        .id(1L)
                        .name("UNFCCC")
                        .description("UNFCCC Description")
                        .version(1)
                        .build();

        ReportingFramework u2 =
                new ReportingFrameworkBuilder()
                        .id(3L)
                        .name("REDD+")
                        .description("REDD+ Description")
                        .version(1)
                        .build();


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/reporting_frameworks/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(u1.getId().toString(), u2.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReportingFramework.class)
                .value(response -> {

                            Assertions.assertThat(response.get(0).getId() == 1L ||
                                    response.get(0).getId() == 3L)
                                    .isTrue();

                            Assertions.assertThat(response.get(0).getName().equals(u1.getName()) ||
                                    response.get(0).getName().equals(u2.getName()))
                                    .isTrue();

                            Assertions.assertThat(response.get(0).getVersion() == 1)
                                    .isTrue();

                            if (response.get(0).getName().equals(u1.getName())) {
                                Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u1.getDescription());
                            } else if (response.get(0).getName().equals(u2.getName())) {
                                Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u2.getDescription());
                            }


                            Assertions.assertThat(response.get(1).getId() == 1L ||
                                    response.get(1).getId() == 3L)
                                    .isTrue();

                            Assertions.assertThat(response.get(1).getVersion() == 1)
                                    .isTrue();

                            if (response.get(1).getName().equals(u1.getName())) {
                                Assertions.assertThat(response.get(1).getDescription()).isEqualTo(u1.getDescription());
                            } else if (response.get(1).getName().equals(u2.getName())) {
                                Assertions.assertThat(response.get(1).getDescription()).isEqualTo(u2.getDescription());
                            }
                        }
                );
    }

    @Test
    public void Given_ReportingFrameworkRecordsExist_When_GetAllWithNameFilter_Then_OnlyReportingFrameworkRecordsWithTheSpecifiedNameWillBeReturned() {

        ReportingFramework u =
                new ReportingFrameworkBuilder()
                        .id(3L)
                        .name("REDD+")
                        .description("REDD+ Description")
                        .version(1)
                        .build();


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/reporting_frameworks/all")
                                .queryParam("name", "{id1}")
                                .build("RE"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReportingFramework.class)
                .value(response -> {

                            Assertions.assertThat(response.get(0).getId() == u.getId()).isTrue();
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(u.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(u.getDescription());
                            Assertions.assertThat(response.get(0).getVersion() == u.getVersion().intValue()).isTrue();

                        }
                );
    }


}
