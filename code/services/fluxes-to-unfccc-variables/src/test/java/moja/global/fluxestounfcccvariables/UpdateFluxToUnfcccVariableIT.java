/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables;

import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.util.builders.FluxesToUnfcccVariableBuilder;
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
@ContextConfiguration(initializers = UpdateFluxToUnfcccVariableIT.Initializer.class)
public class UpdateFluxToUnfcccVariableIT {

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
    public void Given_ModifiedDetailsOfAnExistingRecord_When_Put_Then_TheRecordWillBeUpdatedAndReturnedWithItsVersionIncrementedByOne() {

        FluxToUnfcccVariable u =
                new FluxesToUnfcccVariableBuilder()
                        .id(1L)
                        .startPoolId(1L)
                        .endPoolId(19L)
                        .unfcccVariableId(3L)
                        .rule("add")
                        .version(1)
                        .build();

        webTestClient
                .put()
                .uri("/api/v1/fluxes_to_unfccc_variables")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(u), FluxToUnfcccVariable.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(FluxToUnfcccVariable.class)
                .value(response -> {
                    Assertions.assertThat(response.getId().equals(u.getId())).isTrue();
                    Assertions.assertThat(response.getStartPoolId().equals(u.getStartPoolId())).isTrue();
                    Assertions.assertThat(response.getEndPoolId().equals(u.getEndPoolId())).isTrue();
                    Assertions.assertThat(response.getUnfcccVariableId().equals(u.getUnfcccVariableId())).isTrue();
                    Assertions.assertThat(response.getRule()).isEqualTo(u.getRule());
                    Assertions.assertThat(response.getVersion()).isEqualTo(u.getVersion() + 1);
                        }
                );
    }
}
