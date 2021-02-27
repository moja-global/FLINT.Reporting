/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes;

import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.util.builders.EmissionTypeBuilder;
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
@ContextConfiguration(initializers = CreateEmissionTypesIT.Initializer.class)
public class CreateEmissionTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final EmissionType emissionType4;
    static final EmissionType emissionType5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("emissionTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        emissionType4 =
                new EmissionTypeBuilder()
                        .name("Fourth EmissionType")
                        .abbreviation("A4")
                        .description("Description 4")
                        .build();

        emissionType5 =
                new EmissionTypeBuilder()
                        .name("Fifth EmissionType")
                        .abbreviation("A5")
                        .description("Description 5")
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
    public void Given_EmissionTypeDetailsList_When_PostAll_Then_EmissionTypeRecordsWillBeCreatedAndReturned() {

        EmissionType[] emissionTypes= new EmissionType[]{emissionType4, emissionType5};

        webTestClient
                .post()
                .uri("/api/v1/emission_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(emissionTypes), EmissionType.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(EmissionType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(emissionType4.getName());
                    Assertions.assertThat(response.get(0).getAbbreviation()).isEqualTo(emissionType4.getAbbreviation());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(emissionType4.getDescription());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(emissionType5.getName());
                    Assertions.assertThat(response.get(1).getAbbreviation()).isEqualTo(emissionType5.getAbbreviation());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(emissionType5.getDescription());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
