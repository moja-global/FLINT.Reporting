/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories;

import global.moja.landusecategories.models.LandUseCategory;
import global.moja.landusecategories.util.builders.LandUseCategoryBuilder;
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
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveLandUseCategoryIT.Initializer.class)
public class RetrieveLandUseCategoryIT {

    @Autowired
    WebTestClient webTestClient;


    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseCategory landUseCategory1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("landUseCategories")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        landUseCategory1 =
                new LandUseCategoryBuilder()
                        .id(1L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(null)
                        .coverTypeId(1L)
                        .name("First Land Use Category")
                        .version(1)
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
    public void Given_LandUseCategoryRecordExists_When_GetWithIdParameter_Then_TheLandUseCategoryRecordWithThatIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/ids/{id}")
                                .build(Long.toString(landUseCategory1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LandUseCategory.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(landUseCategory1.getId());
                            Assertions.assertThat(response.getReportingFrameworkId()).isEqualTo(landUseCategory1.getReportingFrameworkId());
                            Assertions.assertThat(response.getParentLandUseCategoryId()).isEqualTo(0L);
                            Assertions.assertThat(response.getCoverTypeId()).isEqualTo(landUseCategory1.getCoverTypeId());
                            Assertions.assertThat(response.getName()).isEqualTo(landUseCategory1.getName());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
