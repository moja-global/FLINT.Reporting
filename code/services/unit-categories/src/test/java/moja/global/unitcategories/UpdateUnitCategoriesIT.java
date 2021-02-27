/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories;

import moja.global.unitcategories.models.UnitCategory;
import moja.global.unitcategories.util.builders.UnitCategoryBuilder;
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
@ContextConfiguration(initializers = UpdateUnitCategoriesIT.Initializer.class)
public class UpdateUnitCategoriesIT {

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

        UnitCategory u1 = new UnitCategoryBuilder().id(1L).name("AREA").version(1).build();
        UnitCategory u2 = new UnitCategoryBuilder().id(2L).name("WEIGHT").version(1).build();
        UnitCategory[] unitCategories = new UnitCategory[]{u1, u2};

        webTestClient
                .put()
                .uri("/api/v1/unit_categories/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(unitCategories), UnitCategory.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UnitCategory.class)
                .value(response -> {

                            Assertions.assertThat(response.get(0).getId() == 1L || response.get(0).getId() == 2L);

                            if (response.get(0).getId() == 1L) {
                                Assertions.assertThat(response.get(0).getName())
                                        .isEqualTo(u1.getName());
                                Assertions.assertThat(response.get(0).getVersion())
                                        .isEqualTo(u1.getVersion() + 1);
                            } else if (response.get(0).getId() == 2L) {
                                Assertions.assertThat(response.get(0).getName())
                                        .isEqualTo(u2.getName());
                                Assertions.assertThat(response.get(0).getVersion())
                                        .isEqualTo(u2.getVersion() + 1);
                            }


                            Assertions.assertThat(response.get(1).getId() == 1L || response.get(1).getId() == 2L);

                            if (response.get(1).getId() == 1L) {
                                Assertions.assertThat(response.get(1).getName())
                                        .isEqualTo(u1.getName());
                                Assertions.assertThat(response.get(1).getVersion())
                                        .isEqualTo(u1.getVersion() + 1);
                            } else if (response.get(1).getId() == 2L) {
                                Assertions.assertThat(response.get(1).getName())
                                        .isEqualTo(u2.getName());
                                Assertions.assertThat(response.get(1).getVersion())
                                        .isEqualTo(u2.getVersion() + 1);
                            }


                        }
                );
    }
}
