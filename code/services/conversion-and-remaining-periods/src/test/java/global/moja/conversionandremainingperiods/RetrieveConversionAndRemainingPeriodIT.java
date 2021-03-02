/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods;

import global.moja.conversionandremainingperiods.util.builders.ConversionAndRemainingPeriodBuilder;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveConversionAndRemainingPeriodIT.Initializer.class)
public class RetrieveConversionAndRemainingPeriodIT {

    @Autowired
    WebTestClient webTestClient;


    static final PostgreSQLContainer postgreSQLContainer;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("conversionAndRemainingPeriods")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        conversionAndRemainingPeriod1 =
                new ConversionAndRemainingPeriodBuilder()
                        .id(1L)
                        .previousLandCoverId(1L)
                        .currentLandCoverId(1L)
                        .conversionPeriod(1)
                        .remainingPeriod(1)
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
    public void Given_ConversionAndRemainingPeriodRecordExists_When_GetWithIdParameter_Then_TheConversionAndRemainingPeriodRecordWithThatIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/conversion_and_remaining_periods/ids/{id}")
                                .build(Long.toString(conversionAndRemainingPeriod1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConversionAndRemainingPeriod.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(conversionAndRemainingPeriod1.getId());
                            Assertions.assertThat(response.getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getPreviousLandCoverId());
                            Assertions.assertThat(response.getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getCurrentLandCoverId());
                            Assertions.assertThat(response.getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod1.getConversionPeriod());
                            Assertions.assertThat(response.getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod1.getRemainingPeriod());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
