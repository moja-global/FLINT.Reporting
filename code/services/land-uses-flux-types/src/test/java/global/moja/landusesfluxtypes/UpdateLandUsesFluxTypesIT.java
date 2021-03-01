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
@ContextConfiguration(initializers = UpdateLandUsesFluxTypesIT.Initializer.class)
public class UpdateLandUsesFluxTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxType landUseFluxType1;
    static final LandUseFluxType landUseFluxType2;

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
                        .landUseCategoryId(4L)
                        .fluxTypeId(4L)
                        .version(1)
                        .build();

        landUseFluxType2 =
                new LandUseFluxTypeBuilder()
                        .id(2L)
                        .landUseCategoryId(5L)
                        .fluxTypeId(5L)
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



        LandUseFluxType[] landUsesFluxTypes= new LandUseFluxType[]{landUseFluxType1, landUseFluxType2};

        webTestClient
                .put()
                .uri("/api/v1/land_uses_flux_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(landUsesFluxTypes), LandUseFluxType.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(LandUseFluxType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxType1.getId());
                    Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(landUseFluxType1.getLandUseCategoryId());
                    Assertions.assertThat(response.get(0).getFluxTypeId()).isEqualTo(landUseFluxType1.getFluxTypeId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(landUseFluxType1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseFluxType2.getId());
                    Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(landUseFluxType2.getLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getFluxTypeId()).isEqualTo(landUseFluxType2.getFluxTypeId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(landUseFluxType2.getVersion() + 1);


                        }
                );
    }
}
