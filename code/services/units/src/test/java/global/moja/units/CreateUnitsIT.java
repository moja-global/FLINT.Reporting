/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units;

import global.moja.units.models.Unit;
import global.moja.units.util.builders.UnitBuilder;
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
@ContextConfiguration(initializers = CreateUnitsIT.Initializer.class)
public class CreateUnitsIT {

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
    public void Given_UnitDetailsList_When_PostAll_Then_UnitRecordsWillBeCreatedAndReturned() {

        Unit u1 =
                new UnitBuilder()
                        .unitCategoryId(2L)
                        .name("Kilogram")
                        .plural("Kilograms")
                        .symbol("kg")
                        .scaleFactor(1.0)
                        .build();

        Unit u2 =
                new UnitBuilder()
                        .unitCategoryId(2L)
                        .name("Ton")
                        .plural("Tons")
                        .symbol("t")
                        .scaleFactor(1000.0)
                        .build();

        Unit[] units = new Unit[]{u1, u2};

        webTestClient
                .post()
                .uri("/api/v1/units/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(units), Unit.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Unit.class)
                .value(response -> {

                    Assertions.assertThat(response.get(0).getId() == 4L || response.get(0).getId() == 5L);
                    Assertions.assertThat(response.get(0).getName().equals("Kilogram") || response.get(0).getName().equals("Ton"));
                    Assertions.assertThat(response.get(0).getVersion() == 1);

                    if (response.get(0).getName().equals("Kilogram")) {
                        Assertions.assertThat(response.get(0).getUnitCategoryId())
                                .isEqualTo(u1.getUnitCategoryId());
                        Assertions.assertThat(response.get(0).getPlural())
                                .isEqualTo(u1.getPlural());
                        Assertions.assertThat(response.get(0).getSymbol())
                                .isEqualTo(u1.getSymbol());
                        Assertions.assertThat(response.get(0).getScaleFactor())
                                .isEqualTo(u1.getScaleFactor());
                    } else if (response.get(0).getName().equals("Kilograms")) {
                        Assertions.assertThat(response.get(0).getUnitCategoryId())
                                .isEqualTo(u2.getUnitCategoryId());
                        Assertions.assertThat(response.get(0).getPlural())
                                .isEqualTo(u2.getPlural());
                        Assertions.assertThat(response.get(0).getSymbol())
                                .isEqualTo(u2.getSymbol());
                        Assertions.assertThat(response.get(0).getScaleFactor())
                                .isEqualTo(u2.getScaleFactor());
                    }

                    Assertions.assertThat(response.get(1).getId() == 4L || response.get(1).getId() == 5L);
                    Assertions.assertThat(response.get(1).getName().equals("Kilogram") || response.get(1).getName().equals("Ton"));
                    Assertions.assertThat(response.get(1).getVersion() == 1);


                    if (response.get(1).getName().equals("Kilogram")) {
                        Assertions.assertThat(response.get(1).getUnitCategoryId())
                                .isEqualTo(u1.getUnitCategoryId());
                        Assertions.assertThat(response.get(1).getPlural())
                                .isEqualTo(u1.getPlural());
                        Assertions.assertThat(response.get(1).getSymbol())
                                .isEqualTo(u1.getSymbol());
                        Assertions.assertThat(response.get(1).getScaleFactor())
                                .isEqualTo(u1.getScaleFactor());
                    } else if (response.get(1).getName().equals("Kilograms")) {
                        Assertions.assertThat(response.get(1).getUnitCategoryId())
                                .isEqualTo(u2.getUnitCategoryId());
                        Assertions.assertThat(response.get(1).getPlural())
                                .isEqualTo(u2.getPlural());
                        Assertions.assertThat(response.get(1).getSymbol())
                                .isEqualTo(u2.getSymbol());
                        Assertions.assertThat(response.get(1).getScaleFactor())
                                .isEqualTo(u2.getScaleFactor());
                    }

                });
    }
}
