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
@ContextConfiguration(initializers = CreateLandUsesFluxTypesIT.Initializer.class)
public class CreateLandUsesFluxTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxType landUseFluxType4;
    static final LandUseFluxType landUseFluxType5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("landUsesFluxTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        landUseFluxType4 =
                new LandUseFluxTypeBuilder()
                        .id(null)
                        .fluxTypeId(4L)
                        .landUseCategoryId(4L)
                        .version(null)
                        .build();

        landUseFluxType5 =
                new LandUseFluxTypeBuilder()
                        .id(null)
                        .fluxTypeId(5L)
                        .landUseCategoryId(5L)
                        .version(null)
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
    public void Given_LandUseFluxTypeDetailsList_When_PostAll_Then_LandUseFluxTypeRecordsWillBeCreatedAndReturned() {

        LandUseFluxType[] landUsesFluxTypes= new LandUseFluxType[]{landUseFluxType4, landUseFluxType5};

        webTestClient
                .post()
                .uri("/api/v1/land_uses_flux_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(landUsesFluxTypes), LandUseFluxType.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(LandUseFluxType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getLandUseCategoryId()).isEqualTo(landUseFluxType4.getLandUseCategoryId());
                    Assertions.assertThat(response.get(0).getFluxTypeId()).isEqualTo(landUseFluxType4.getFluxTypeId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getLandUseCategoryId()).isEqualTo(landUseFluxType5.getLandUseCategoryId());
                    Assertions.assertThat(response.get(1).getFluxTypeId()).isEqualTo(landUseFluxType5.getFluxTypeId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
