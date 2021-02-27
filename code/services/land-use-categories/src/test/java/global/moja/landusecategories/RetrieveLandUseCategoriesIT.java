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

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveLandUseCategoriesIT.Initializer.class)
public class RetrieveLandUseCategoriesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseCategory landUseCategory1;
    static final LandUseCategory landUseCategory2;
    static final LandUseCategory landUseCategory3;

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

        landUseCategory2 =
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Second Land Use Category")
                        .version(1)
                        .build();

        landUseCategory3 =
                new LandUseCategoryBuilder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Third Land Use Category")
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
    public void Given_LandUseCategoryRecordsExist_When_GetAllWithIdsFilter_Then_OnlyLandUseCategoryRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(landUseCategory1.getId().toString(), landUseCategory3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory1.getId());
                            Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory1.getReportingFrameworkId());
                            Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(0L);
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory1.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory1.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseCategory3.getId());
                            Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory3.getReportingFrameworkId());
                            Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory3.getParentLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory3.getCoverTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory3.getName());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_LandUseCategoryRecordsExist_When_GetAllWithReportingFrameworkIdFilter_Then_OnlyLandUseCategoryRecordsWithTheSpecifiedReportingFrameworkIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/all")
                                .queryParam("reportingFrameworkId", "{id1}")
                                .build(landUseCategory3.getReportingFrameworkId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                            Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory1.getId());
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory1.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(0L);
                    Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory1.getCoverTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory1.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseCategory2.getId());
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory2.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory2.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory2.getCoverTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory2.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(2).getId()).isEqualTo(landUseCategory3.getId());
                    Assertions.assertThat(response.get(2).getReportingFrameworkId()).isEqualTo(landUseCategory3.getReportingFrameworkId());
                    Assertions.assertThat(response.get(2).getParentLandUseCategoryId()).isEqualTo(landUseCategory3.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(2).getCoverTypeId()).isEqualTo(landUseCategory3.getCoverTypeId());
                    Assertions.assertThat(response.get(2).getName()).isEqualTo(landUseCategory3.getName());
                    Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseCategoryRecordsExist_When_GetAllWithParentLandUseCategoryIdFilter_Then_OnlyLandUseCategoryRecordsWithTheSpecifiedParentLandUseCategoryIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/all")
                                .queryParam("parentLandUseCategoryId", "{id1}")
                                .build(landUseCategory3.getParentLandUseCategoryId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                            Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory2.getId());
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory2.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(landUseCategory2.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory2.getCoverTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory2.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseCategory3.getId());
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory3.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory3.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory3.getCoverTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory3.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseCategoryRecordsExist_When_GetAllWithCoverTypeIdFilter_Then_OnlyLandUseCategoryRecordsWithTheSpecifiedCoverTypeIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/all")
                                .queryParam("coverTypeId", "{id1}")
                                .build(landUseCategory3.getCoverTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                            Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory1.getId());
                    Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory1.getReportingFrameworkId());
                    Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(0L);
                    Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory1.getCoverTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory1.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseCategory2.getId());
                    Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory2.getReportingFrameworkId());
                    Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory2.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory2.getCoverTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory2.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(2).getId()).isEqualTo(landUseCategory3.getId());
                    Assertions.assertThat(response.get(2).getReportingFrameworkId()).isEqualTo(landUseCategory3.getReportingFrameworkId());
                    Assertions.assertThat(response.get(2).getParentLandUseCategoryId()).isEqualTo(landUseCategory3.getParentLandUseCategoryId());
                    Assertions.assertThat(response.get(2).getCoverTypeId()).isEqualTo(landUseCategory3.getCoverTypeId());
                    Assertions.assertThat(response.get(2).getName()).isEqualTo(landUseCategory3.getName());
                    Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseCategoryRecordsExist_When_GetAllWithNameFilter_Then_OnlyLandUseCategoryRecordsWithTheSpecifiedNameWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory3.getId());
                            Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory3.getReportingFrameworkId());
                            Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(landUseCategory3.getParentLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory3.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory3.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseCategoryRecordsExist_When_GetAllWithoutFilters_Then_AllLandUseCategoryRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_use_categories/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseCategory.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseCategory1.getId());
                            Assertions.assertThat(response.get(0).getReportingFrameworkId()).isEqualTo(landUseCategory1.getReportingFrameworkId());
                            Assertions.assertThat(response.get(0).getParentLandUseCategoryId()).isEqualTo(0L);
                            Assertions.assertThat(response.get(0).getCoverTypeId()).isEqualTo(landUseCategory1.getCoverTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(landUseCategory1.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseCategory2.getId());
                            Assertions.assertThat(response.get(1).getReportingFrameworkId()).isEqualTo(landUseCategory2.getReportingFrameworkId());
                            Assertions.assertThat(response.get(1).getParentLandUseCategoryId()).isEqualTo(landUseCategory2.getParentLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getCoverTypeId()).isEqualTo(landUseCategory2.getCoverTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(landUseCategory2.getName());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(landUseCategory3.getId());
                            Assertions.assertThat(response.get(2).getReportingFrameworkId()).isEqualTo(landUseCategory3.getReportingFrameworkId());
                            Assertions.assertThat(response.get(2).getParentLandUseCategoryId()).isEqualTo(landUseCategory3.getParentLandUseCategoryId());
                            Assertions.assertThat(response.get(2).getCoverTypeId()).isEqualTo(landUseCategory3.getCoverTypeId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(landUseCategory3.getName());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
