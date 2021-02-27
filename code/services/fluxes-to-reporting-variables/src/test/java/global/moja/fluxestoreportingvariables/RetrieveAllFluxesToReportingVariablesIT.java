/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables;

import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
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
@ContextConfiguration(initializers = RetrieveAllFluxesToReportingVariablesIT.Initializer.class)
public class RetrieveAllFluxesToReportingVariablesIT {

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
    public void Given_FluxesToUnfcccVariableRecordsExist_When_GetAllWithoutIdsFilter_Then_AllUnfcccVariableRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri("/api/v1/fluxes_to_reporting_variables/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxToReportingVariable.class)
                .value(response -> {
                            Assertions.assertThat(response.get(0).getId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(0).getStartPoolId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(0).getEndPoolId())
                                    .isEqualTo(18L);
                            Assertions.assertThat(response.get(0).getReportingVariableId())
                                    .isEqualTo(2L);
                            Assertions.assertThat(response.get(0).getVersion())
                                    .isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId())
                                    .isEqualTo(2L);
                            Assertions.assertThat(response.get(1).getStartPoolId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(1).getEndPoolId())
                                    .isEqualTo(19L);
                            Assertions.assertThat(response.get(1).getReportingVariableId())
                                    .isEqualTo(3L);
                            Assertions.assertThat(response.get(1).getVersion())
                                    .isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId())
                                    .isEqualTo(3L);
                            Assertions.assertThat(response.get(2).getStartPoolId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(2).getEndPoolId())
                                    .isEqualTo(22L);
                            Assertions.assertThat(response.get(2).getReportingVariableId())
                                    .isEqualTo(4L);
                            Assertions.assertThat(response.get(2).getVersion())
                                    .isEqualTo(1);
                        }
                );
    }
}
