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
@ContextConfiguration(initializers = CreateLandUseCategoriesIT.Initializer.class)
public class CreateLandUseCategoriesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseCategory landUseCategory4;
    static final LandUseCategory landUseCategory5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("landUseCategories")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        landUseCategory4 =
                new LandUseCategoryBuilder()
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(null)
                        .coverTypeId(1L)
                        .name("Fourth Land Use Category")
                        .build();

        landUseCategory5 =
                new LandUseCategoryBuilder()
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Fifth Land Use Category")
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
    public void Given_LandUseCategoryDetailsList_When_PostAll_Then_LandUseCategoryRecordsWillBeCreatedAndReturned() {

        LandUseCategory[] landUseCategories= new LandUseCategory[]{landUseCategory4, landUseCategory5};

        webTestClient
                .post()
                .uri("/api/v1/land_use_categories/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(landUseCategories), LandUseCategory.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory4.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(0L);
                    Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory4.getCoverTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory4.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory5.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory5.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory5.getCoverTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory5.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
