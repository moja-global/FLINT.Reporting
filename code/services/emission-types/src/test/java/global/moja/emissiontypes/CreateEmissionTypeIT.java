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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateEmissionTypeIT.Initializer.class)
public class CreateEmissionTypeIT {

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final EmissionType emissionType4;

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
                        .id(null)
                        .name("Fourth EmissionType")
                        .abbreviation("A4")
                        .description("Description 4")
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
    public void Given_EmissionTypeDetails_When_Post_Then_EmissionTypeRecordWillBeCreatedAndReturned() {

        webTestClient
                .post()
                .uri("/api/v1/emission_types")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(emissionType4), EmissionType.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmissionType.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(4L);
                            Assertions.assertThat(response.getName()).isEqualTo(emissionType4.getName());
                            Assertions.assertThat(response.getAbbreviation()).isEqualTo(emissionType4.getAbbreviation());
                            Assertions.assertThat(response.getDescription()).isEqualTo(emissionType4.getDescription());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
