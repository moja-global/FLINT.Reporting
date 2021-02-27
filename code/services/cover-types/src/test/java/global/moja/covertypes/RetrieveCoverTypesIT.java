/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.util.builders.CoverTypeBuilder;
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
@ContextConfiguration(initializers = RetrieveCoverTypesIT.Initializer.class)
public class RetrieveCoverTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final CoverType coverType1;
    static final CoverType coverType2;
    static final CoverType coverType3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("coverTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        coverType1 =
                new CoverTypeBuilder()
                        .id(1L)
                        .code("Fi")
                        .description("First CoverType")
                        .version(1)
                        .build();

        coverType2 =
                new CoverTypeBuilder()
                        .id(2L)
                        .code("Se")
                        .description("Second CoverType")
                        .version(1)
                        .build();

        coverType3 =
                new CoverTypeBuilder()
                        .id(3L)
                        .code("Th")
                        .description("Third CoverType")
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
    public void Given_CoverTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyCoverTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/cover_types/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(coverType1.getId().toString(), coverType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CoverType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(coverType1.getId());
                            Assertions.assertThat(response.get(0).getCode()).isEqualTo(coverType1.getCode());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(coverType1.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(coverType3.getId());
                            Assertions.assertThat(response.get(1).getCode()).isEqualTo(coverType3.getCode());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(coverType3.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }

    @Test
    public void Given_CoverTypeRecordsExist_When_GetAllWithCodeFilter_Then_OnlyCoverTypeRecordsWithTheSpecifiedCodeWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/cover_types/all")
                                .queryParam("code", "{id1}")
                                .build("Th"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CoverType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(coverType3.getId());
                            Assertions.assertThat(response.get(0).getCode()).isEqualTo(coverType3.getCode());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(coverType3.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_CoverTypeRecordsExist_When_GetAllWithoutFilters_Then_AllCoverTypeRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/cover_types/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CoverType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(coverType1.getId());
                            Assertions.assertThat(response.get(0).getCode()).isEqualTo(coverType1.getCode());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(coverType1.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(coverType2.getId());
                            Assertions.assertThat(response.get(1).getCode()).isEqualTo(coverType2.getCode());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(coverType2.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(coverType3.getId());
                            Assertions.assertThat(response.get(2).getCode()).isEqualTo(coverType3.getCode());
                            Assertions.assertThat(response.get(2).getDescription()).isEqualTo(coverType3.getDescription());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
