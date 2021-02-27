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
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateFluxesToReportingVariablesIT.Initializer.class)
public class CreateFluxesToReportingVariablesIT {

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
    public void Given_FluxToReportingVariableDetailsList_When_PostAll_Then_FluxesToUnfcccVariableRecordsWillBeCreatedAndReturned() {

        FluxToReportingVariable u1 =
                new FluxesToReportingVariableBuilder()
                        .startPoolId(2L)
                        .endPoolId(5L)
                        .reportingVariableId(3L)
                        .rule("ignore")
                        .build();

        FluxToReportingVariable u2 =
                new FluxesToReportingVariableBuilder()
                        .startPoolId(3L)
                        .endPoolId(6L)
                        .reportingVariableId(4L)
                        .rule("subtract")
                        .build();

        FluxToReportingVariable[] fluxToReportingVariables = new FluxToReportingVariable[]{u1, u2};

        webTestClient
                .post()
                .uri("/api/v1/fluxes_to_reporting_variables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fluxToReportingVariables), FluxToReportingVariable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(FluxToReportingVariable.class)
                .value(response -> {

                    Assertions.assertThat(response.get(0).getId() == 4L ||
                            response.get(0).getId() == 5L)
                            .isTrue();

                    Assertions.assertThat(response.get(0).getStartPoolId().equals(u1.getStartPoolId()) ||
                            response.get(0).getStartPoolId().equals(u2.getStartPoolId()))
                            .isTrue();

                    Assertions.assertThat(response.get(0).getVersion() == 1).isTrue();

                    if (response.get(0).getStartPoolId().equals(u1.getStartPoolId())) {
                        Assertions.assertThat(response.get(0).getEndPoolId().equals(u1.getEndPoolId())).isTrue();
                        Assertions.assertThat(response.get(0).getReportingVariableId().equals(u1.getReportingVariableId())).isTrue();
                        Assertions.assertThat(response.get(0).getRule()).isEqualTo(u1.getRule());
                    } else if (response.get(0).getStartPoolId().equals(u2.getStartPoolId())) {
                        Assertions.assertThat(response.get(0).getEndPoolId().equals(u2.getEndPoolId())).isTrue();
                        Assertions.assertThat(response.get(0).getReportingVariableId().equals(u2.getReportingVariableId())).isTrue();
                        Assertions.assertThat(response.get(0).getRule()).isEqualTo(u2.getRule());
                    }

                    Assertions.assertThat(response.get(1).getId() == 4L ||
                            response.get(1).getId() == 5L)
                            .isTrue();

                    Assertions.assertThat(response.get(1).getStartPoolId().equals(u1.getStartPoolId()) ||
                            response.get(1).getStartPoolId().equals(u2.getStartPoolId()))
                            .isTrue();

                    Assertions.assertThat(response.get(1).getVersion() == 1).isTrue();

                    if (response.get(1).getStartPoolId().equals(u1.getStartPoolId())) {
                        Assertions.assertThat(response.get(1).getEndPoolId().equals(u1.getEndPoolId())).isTrue();
                        Assertions.assertThat(response.get(1).getReportingVariableId().equals(u1.getReportingVariableId())).isTrue();
                        Assertions.assertThat(response.get(1).getRule()).isEqualTo(u1.getRule());
                    } else if (response.get(1).getStartPoolId().equals(u2.getStartPoolId())) {
                        Assertions.assertThat(response.get(1).getEndPoolId().equals(u2.getEndPoolId())).isTrue();
                        Assertions.assertThat(response.get(1).getReportingVariableId().equals(u2.getReportingVariableId())).isTrue();
                        Assertions.assertThat(response.get(1).getRule()).isEqualTo(u2.getRule());
                    }
                });
    }
}
