/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes;

import global.moja.fluxtypes.models.FluxType;
import global.moja.fluxtypes.util.builders.FluxTypeBuilder;
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
@ContextConfiguration(initializers = RetrieveFluxTypesIT.Initializer.class)
public class RetrieveFluxTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final FluxType fluxType1;
    static final FluxType fluxType2;
    static final FluxType fluxType3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("fluxTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        fluxType1 =
                new FluxTypeBuilder()
                        .id(1L)
                        .name("First FluxType")
                        .description(null)
                        .version(1)
                        .build();

        fluxType2 =
                new FluxTypeBuilder()
                        .id(2L)
                        .name("Second FluxType")
                        .description(null)
                        .version(1)
                        .build();

        fluxType3 =
                new FluxTypeBuilder()
                        .id(3L)
                        .name("Third FluxType")
                        .description("Third FluxType Description")
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
    public void Given_FluxTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyFluxTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_types/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(fluxType1.getId().toString(), fluxType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(fluxType1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(fluxType1.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isNull();
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(fluxType3.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(fluxType3.getName());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(fluxType3.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }

    @Test
    public void Given_FluxTypeRecordsExist_When_GetAllWithNameFilter_Then_OnlyFluxTypeRecordsWithTheSpecifiedNameWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_types/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(fluxType3.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(fluxType3.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(fluxType3.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_FluxTypeRecordsExist_When_GetAllWithoutFilters_Then_AllFluxTypeRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/flux_types/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FluxType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(fluxType1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(fluxType1.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isNull();
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(fluxType2.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(fluxType2.getName());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(fluxType2.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(fluxType3.getId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(fluxType3.getName());
                            Assertions.assertThat(response.get(2).getDescription()).isEqualTo(fluxType3.getDescription());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
