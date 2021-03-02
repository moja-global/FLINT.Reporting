/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods;

import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import global.moja.conversionandremainingperiods.util.builders.ConversionAndRemainingPeriodBuilder;
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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateConversionAndRemainingPeriodIT.Initializer.class)
public class CreateConversionAndRemainingPeriodIT {

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod4;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("conversionAndRemainingPeriods")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        conversionAndRemainingPeriod4 =
                new ConversionAndRemainingPeriodBuilder()
                        .id(null)
                        .previousLandCoverId(4L)
                        .currentLandCoverId(4L)
                        .conversionPeriod(4)
                        .remainingPeriod(4)
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
    public void Given_ConversionAndRemainingPeriodDetails_When_Post_Then_ConversionAndRemainingPeriodRecordWillBeCreatedAndReturned() {

        webTestClient
                .post()
                .uri("/api/v1/conversion_and_remaining_periods")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(conversionAndRemainingPeriod4), ConversionAndRemainingPeriod.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConversionAndRemainingPeriod.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(4L);
                            Assertions.assertThat(response.getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod4.getPreviousLandCoverId());
                            Assertions.assertThat(response.getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod4.getCurrentLandCoverId());
                            Assertions.assertThat(response.getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod4.getConversionPeriod());
                            Assertions.assertThat(response.getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod4.getRemainingPeriod());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
