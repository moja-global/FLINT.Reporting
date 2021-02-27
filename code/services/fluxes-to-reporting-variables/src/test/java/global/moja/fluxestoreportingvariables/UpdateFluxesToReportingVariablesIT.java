/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables;

import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
import global.moja.fluxestoreportingvariables.util.builders.FluxesToReportingVariableBuilder;
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
@ContextConfiguration(initializers = UpdateFluxesToReportingVariablesIT.Initializer.class)
public class UpdateFluxesToReportingVariablesIT {

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

        FluxToReportingVariable u1 =
                new FluxesToReportingVariableBuilder()
                        .id(1L)
                        .startPoolId(1L)
                        .endPoolId(19L)
                        .reportingVariableId(3L)
                        .rule("add")
                        .version(1)
                        .build();


        FluxToReportingVariable u2 =
                new FluxesToReportingVariableBuilder()
                        .id(2L)
                        .startPoolId(1L)
                        .endPoolId(18L)
                        .reportingVariableId(2L)
                        .rule("subtract")
                        .version(1)
                        .build();


        FluxToReportingVariable[] fluxToReportingVariables = new FluxToReportingVariable[]{u1, u2};

        webTestClient
                .put()
                .uri("/api/v1/fluxes_to_reporting_variables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fluxToReportingVariables), FluxToReportingVariable.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(FluxToReportingVariable.class)
                .value(response -> {

                            Assertions.assertThat(response.get(0).getId() == 1L ||
                                    response.get(0).getId() == 2L).isTrue();

                            if (response.get(0).getId() == 1L) {
                                Assertions.assertThat(response.get(0).getStartPoolId().equals(u1.getStartPoolId())).isTrue();
                                Assertions.assertThat(response.get(0).getEndPoolId().equals(u1.getEndPoolId())).isTrue();
                                Assertions.assertThat(response.get(0).getReportingVariableId().equals(u1.getReportingVariableId())).isTrue();
                                Assertions.assertThat(response.get(0).getRule()).isEqualTo(u1.getRule());
                                Assertions.assertThat(response.get(0).getVersion()).isEqualTo(u1.getVersion() + 1);
                            } else if (response.get(0).getId() == 2L) {
                                Assertions.assertThat(response.get(0).getStartPoolId().equals(u2.getStartPoolId())).isTrue();
                                Assertions.assertThat(response.get(0).getEndPoolId().equals(u2.getEndPoolId())).isTrue();
                                Assertions.assertThat(response.get(0).getReportingVariableId().equals(u2.getReportingVariableId())).isTrue();
                                Assertions.assertThat(response.get(0).getRule()).isEqualTo(u2.getRule());
                                Assertions.assertThat(response.get(0).getVersion()).isEqualTo(u2.getVersion() + 1);
                            }

                            Assertions.assertThat(response.get(1).getId() == 1L ||
                                    response.get(1).getId() == 2L).isTrue();

                            if (response.get(1).getId() == 1L) {
                                Assertions.assertThat(response.get(1).getStartPoolId().equals(u1.getStartPoolId())).isTrue();
                                Assertions.assertThat(response.get(1).getEndPoolId().equals(u1.getEndPoolId())).isTrue();
                                Assertions.assertThat(response.get(1).getReportingVariableId().equals(u1.getReportingVariableId())).isTrue();
                                Assertions.assertThat(response.get(1).getRule()).isEqualTo(u1.getRule());
                                Assertions.assertThat(response.get(1).getVersion()).isEqualTo(u1.getVersion() + 1);
                            } else if (response.get(1).getId() == 2L) {
                                Assertions.assertThat(response.get(1).getStartPoolId().equals(u2.getStartPoolId())).isTrue();
                                Assertions.assertThat(response.get(1).getEndPoolId().equals(u2.getEndPoolId())).isTrue();
                                Assertions.assertThat(response.get(1).getReportingVariableId().equals(u2.getReportingVariableId())).isTrue();
                                Assertions.assertThat(response.get(1).getRule()).isEqualTo(u2.getRule());
                                Assertions.assertThat(response.get(1).getVersion()).isEqualTo(u2.getVersion() + 1);
                            }


                        }
                );
    }
}
