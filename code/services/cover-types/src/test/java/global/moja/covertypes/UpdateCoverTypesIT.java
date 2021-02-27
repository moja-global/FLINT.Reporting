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
@ContextConfiguration(initializers = UpdateCoverTypesIT.Initializer.class)
public class UpdateCoverTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final CoverType coverType1;
    static final CoverType coverType2;

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
                        .code("FI")
                        .description("FIRST COVER TYPE")
                        .version(1)
                        .build();

        coverType2 =
                new CoverTypeBuilder()
                        .id(2L)
                        .code("SE")
                        .description("SECOND COVER TYPE")
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



        CoverType[] coverTypes= new CoverType[]{coverType1, coverType2};

        webTestClient
                .put()
                .uri("/api/v1/cover_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(coverTypes), CoverType.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CoverType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(coverType1.getId());
                    Assertions.assertThat(response.get(0).getCode()).isEqualTo(coverType1.getCode());
                    Assertions.assertThat(response.get(0).getDescription()).isEqualTo(coverType1.getDescription());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(coverType1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(coverType2.getId());
                    Assertions.assertThat(response.get(1).getCode()).isEqualTo(coverType2.getCode());
                    Assertions.assertThat(response.get(1).getDescription()).isEqualTo(coverType2.getDescription());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(coverType2.getVersion() + 1);


                        }
                );
    }
}
