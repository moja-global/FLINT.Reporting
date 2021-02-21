/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.units;

import moja.global.units.models.Unit;
import moja.global.units.util.builders.UnitBuilder;
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
@ContextConfiguration(initializers = UpdateUnitIT.Initializer.class)
public class UpdateUnitIT {

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

        Unit u =
                new UnitBuilder()
                        .id(1L)
                        .unitCategoryId(1L)
                        .name("SQUARE METRE")
                        .plural("SQUARE METRE")
                        .symbol("M2")
                        .scaleFactor(1.0)
                        .version(1)
                        .build();

        webTestClient
                .put()
                .uri("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(u), Unit.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Unit.class)
                .value(response -> {
                    Assertions.assertThat(response.getUnitCategoryId())
                            .isEqualTo(u.getUnitCategoryId());
                    Assertions.assertThat(response.getName())
                            .isEqualTo(u.getName());
                    Assertions.assertThat(response.getPlural())
                            .isEqualTo(u.getPlural());
                    Assertions.assertThat(response.getSymbol())
                            .isEqualTo(u.getSymbol());
                    Assertions.assertThat(response.getScaleFactor())
                            .isEqualTo(u.getScaleFactor());
                    Assertions.assertThat(response.getVersion())
                            .isEqualTo(u.getVersion() + 1);
                        }
                );
    }
}
