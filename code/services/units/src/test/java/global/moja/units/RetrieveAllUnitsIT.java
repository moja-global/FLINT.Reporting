/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units;

import global.moja.units.models.Unit;
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
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveAllUnitsIT.Initializer.class)
public class RetrieveAllUnitsIT {

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
    public void Given_UnitRecordsExist_When_GetAllWithoutIdsFilter_Then_AllUnitRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri("/api/v1/units/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Unit.class)
                .value(response -> {
                            Assertions.assertThat(response.get(0).getId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(0).getUnitCategoryId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(0).getName())
                                    .isEqualTo("Square Metre");
                            Assertions.assertThat(response.get(0).getPlural())
                                    .isEqualTo("Square Metres");
                            Assertions.assertThat(response.get(0).getSymbol())
                                    .isEqualTo("m2");
                            Assertions.assertThat(response.get(0).getScaleFactor())
                                    .isEqualTo(1);
                            Assertions.assertThat(response.get(0).getVersion())
                                    .isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId())
                                    .isEqualTo(2L);
                            Assertions.assertThat(response.get(1).getUnitCategoryId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(1).getName())
                                    .isEqualTo("Hectare");
                            Assertions.assertThat(response.get(1).getPlural())
                                    .isEqualTo("Hectares");
                            Assertions.assertThat(response.get(1).getSymbol())
                                    .isEqualTo("ha");
                            Assertions.assertThat(response.get(1).getScaleFactor())
                                    .isEqualTo(10000);
                            Assertions.assertThat(response.get(1).getVersion())
                                    .isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId())
                                    .isEqualTo(3L);
                            Assertions.assertThat(response.get(2).getUnitCategoryId())
                                    .isEqualTo(1L);
                            Assertions.assertThat(response.get(2).getName())
                                    .isEqualTo("Kilohectare");
                            Assertions.assertThat(response.get(2).getPlural())
                                    .isEqualTo("Kilohectares");
                            Assertions.assertThat(response.get(2).getSymbol())
                                    .isEqualTo("kha");
                            Assertions.assertThat(response.get(2).getScaleFactor())
                                    .isEqualTo(10000000);
                            Assertions.assertThat(response.get(2).getVersion())
                                    .isEqualTo(1);
                        }
                );
    }
}
