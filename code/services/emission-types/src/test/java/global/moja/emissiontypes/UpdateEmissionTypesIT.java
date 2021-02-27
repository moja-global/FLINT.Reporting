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
@ContextConfiguration(initializers = UpdateEmissionTypesIT.Initializer.class)
public class UpdateEmissionTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final EmissionType emissionType1;
    static final EmissionType emissionType2;

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
                        .name("FIRST INDICATOR")
                        .abbreviation("a1")
                        .description(null)
                        .version(1)
                        .build();

        emissionType2 =
                new EmissionTypeBuilder()
                        .id(2L)
                        .name("SECOND INDICATOR")
                        .abbreviation("a2")
                        .description("Description 2")
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



        EmissionType[] emissionTypes= new EmissionType[]{emissionType1, emissionType2};

        webTestClient
                .put()
                .uri("/api/v1/emission_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(emissionTypes), EmissionType.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EmissionType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(emissionType1.getId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(emissionType1.getName());
                    Assertions.assertThat(response.get(0).getAbbreviation()).isEqualTo(emissionType1.getAbbreviation());
                    Assertions.assertThat(response.get(0).getDescription()).isNull();
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(emissionType1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(emissionType2.getId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(emissionType2.getName());
                    Assertions.assertThat(response.get(1).getAbbreviation()).isEqualTo(emissionType2.getAbbreviation());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(emissionType2.getDescription());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(emissionType2.getVersion() + 1);


                        }
                );
    }
}
