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
@ContextConfiguration(initializers = UpdateLandUseCategoriesIT.Initializer.class)
public class UpdateLandUseCategoriesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseCategory landUseCategory1;
    static final LandUseCategory landUseCategory2;

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
                        .reportingFrameworkId(10L)
                        .parentLandUseCategoryId(10L)
                        .coverTypeId(10L)
                        .name("FIRST LAND USE CATEGORY")
                        .version(1)
                        .build();

        landUseCategory2 =
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(20L)
                        .parentLandUseCategoryId(20L)
                        .coverTypeId(20L)
                        .name("SECOND LAND USE CATEGORY")
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
    public void Given_ModifiedDetailsOfExistingRecords_When_PutAll_Then_TheRecordsWillBeUpdatedAndReturnedWithTheirVersionsIncrementedByOne() {



        LandUseCategory[] landUseCategories= new LandUseCategory[]{landUseCategory1, landUseCategory2};

        webTestClient
                .put()
                .uri("/api/v1/land_use_categories/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(landUseCategories), LandUseCategory.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory1.getId());
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory1.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(landUseCategory1.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory1.getCoverTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory1.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(landUseCategory1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseCategory2.getId());
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory2.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory2.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory2.getCoverTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory2.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(landUseCategory2.getVersion() + 1);


                        }
                );
    }
}
