/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes;

import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.util.builders.EmissionTypeBuilder;
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
@ContextConfiguration(initializers = RetrieveEmissionTypesIT.Initializer.class)
public class RetrieveEmissionTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final EmissionType emissionType1;
    static final EmissionType emissionType2;
    static final EmissionType emissionType3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("emissionTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        emissionType1 =
                new EmissionTypeBuilder()
                        .id(1L)
                        .name("First EmissionType")
                        .abbreviation("A1")
                        .description(null)
                        .version(1)
                        .build();

        emissionType2 =
                new EmissionTypeBuilder()
                        .id(2L)
                        .name("Second EmissionType")
                        .abbreviation("A2")
                        .description(null)
                        .version(1)
                        .build();

        emissionType3 =
                new EmissionTypeBuilder()
                        .id(3L)
                        .name("Third EmissionType")
                        .abbreviation("A3")
                        .description("Description 3")
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
    public void Given_EmissionTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyEmissionTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/emission_types/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(emissionType1.getId().toString(), emissionType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmissionType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(emissionType1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(emissionType1.getName());
                            Assertions.assertThat(response.get(0).getAbbreviation()).isEqualTo(emissionType1.getAbbreviation());
                            Assertions.assertThat(response.get(0).getDescription()).isNull();
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(emissionType3.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(emissionType3.getName());
                            Assertions.assertThat(response.get(1).getAbbreviation()).isEqualTo(emissionType3.getAbbreviation());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(emissionType3.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_EmissionTypeRecordsExist_When_GetAllWithNameFilter_Then_OnlyEmissionTypeRecordsWithTheSpecifiedNameWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/emission_types/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmissionType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(emissionType3.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(emissionType3.getName());
                            Assertions.assertThat(response.get(0).getAbbreviation()).isEqualTo(emissionType3.getAbbreviation());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(emissionType3.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_EmissionTypeRecordsExist_When_GetAllWithAbbreviationFilter_Then_OnlyEmissionTypeRecordsWithTheSpecifiedAbbreviationWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/emission_types/all")
                                .queryParam("abbreviation", "{id1}")
                                .build("A3"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmissionType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(emissionType3.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(emissionType3.getName());
                            Assertions.assertThat(response.get(0).getAbbreviation()).isEqualTo(emissionType3.getAbbreviation());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(emissionType3.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_EmissionTypeRecordsExist_When_GetAllWithoutFilters_Then_AllEmissionTypeRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/emission_types/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmissionType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(emissionType1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(emissionType1.getName());
                            Assertions.assertThat(response.get(0).getAbbreviation()).isEqualTo(emissionType1.getAbbreviation());
                            Assertions.assertThat(response.get(0).getDescription()).isNull();
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(emissionType2.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(emissionType2.getName());
                            Assertions.assertThat(response.get(1).getAbbreviation()).isEqualTo(emissionType2.getAbbreviation());
                            Assertions.assertThat(response.get(1).getDescription()).isNull();
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(emissionType3.getId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(emissionType3.getName());
                            Assertions.assertThat(response.get(2).getAbbreviation()).isEqualTo(emissionType3.getAbbreviation());
                            Assertions.assertThat(response.get(2).getDescription()).isEqualTo(emissionType3.getDescription());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
