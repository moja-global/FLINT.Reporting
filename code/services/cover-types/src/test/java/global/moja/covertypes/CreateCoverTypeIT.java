/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.util.builders.CoverTypeBuilder;
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
@ContextConfiguration(initializers = CreateCoverTypeIT.Initializer.class)
public class CreateCoverTypeIT {

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final CoverType coverType4;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("coverTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        coverType4 =
                new CoverTypeBuilder()
                        .id(null)
                        .code("Fo")
                        .description("Fourth CoverType")
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
    public void Given_CoverTypeDetails_When_Post_Then_CoverTypeRecordWillBeCreatedAndReturned() {

        webTestClient
                .post()
                .uri("/api/v1/cover_types")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(coverType4), CoverType.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CoverType.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(4L);
                            Assertions.assertThat(response.getCode()).isEqualTo(coverType4.getCode());
                            Assertions.assertThat(response.getDescription()).isEqualTo(coverType4.getDescription());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
