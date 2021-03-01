/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes;

import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.util.builders.LandUseFluxTypeBuilder;
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
@ContextConfiguration(initializers = RetrieveLandUsesFluxTypesIT.Initializer.class)
public class RetrieveLandUsesFluxTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxType landUseFluxType1;
    static final LandUseFluxType landUseFluxType2;
    static final LandUseFluxType landUseFluxType3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("landUsesFluxTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        landUseFluxType1 =
                new LandUseFluxTypeBuilder()
                        .id(1L)
                        .landUseCategoryId(1L)
                        .fluxTypeId(1L)
                        .version(1)
                        .build();

        landUseFluxType2 =
                new LandUseFluxTypeBuilder()
                        .id(2L)
                        .landUseCategoryId(2L)
                        .fluxTypeId(2L)
                        .version(1)
                        .build();

        landUseFluxType3 =
                new LandUseFluxTypeBuilder()
                        .id(3L)
                        .landUseCategoryId(3L)
                        .fluxTypeId(3L)
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
    public void Given_LandUseFluxTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyLandUseFluxTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(landUseFluxType1.getId().toString(), landUseFluxType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxType1.getId());
                            Assertions.assertThat(response.get(0).getFluxTypeId()).isEqualTo(landUseFluxType1.getFluxTypeId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(landUseFluxType1.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);


                            Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseFluxType3.getId());
                            Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(landUseFluxType3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getFluxTypeId()).isEqualTo(landUseFluxType3.getFluxTypeId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeRecordsExist_When_GetAllWithLandUseCategoryIdFilter_Then_OnlyLandUseFluxTypeRecordsWithTheSpecifiedLandUseCategoryIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types/all")
                                .queryParam("landUseCategoryId", "{id1}")
                                .build(landUseFluxType3.getLandUseCategoryId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxType3.getId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(landUseFluxType3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getFluxTypeId()).isEqualTo(landUseFluxType3.getFluxTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeRecordsExist_When_GetAllWithFluxTypeIdFilter_Then_OnlyLandUseFluxTypeRecordsWithTheSpecifiedFluxTypeIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types/all")
                                .queryParam("fluxTypeId", "{id1}")
                                .build(landUseFluxType3.getFluxTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxType3.getId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(landUseFluxType3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getFluxTypeId()).isEqualTo(landUseFluxType3.getFluxTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeRecordsExist_When_GetAllWithoutFilters_Then_AllLandUseFluxTypeRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxType1.getId());
                            Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(landUseFluxType1.getLandUseCategoryId());
                            Assertions.assertThat(response.get(0).getFluxTypeId()).isEqualTo(landUseFluxType1.getFluxTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseFluxType2.getId());
                            Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(landUseFluxType2.getLandUseCategoryId());
                            Assertions.assertThat(response.get(1).getFluxTypeId()).isEqualTo(landUseFluxType2.getFluxTypeId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(landUseFluxType3.getId());
                            Assertions.assertThat(response.get(2).getLandUseCategoryId()).isEqualTo(landUseFluxType3.getLandUseCategoryId());
                            Assertions.assertThat(response.get(2).getFluxTypeId()).isEqualTo(landUseFluxType3.getFluxTypeId());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
