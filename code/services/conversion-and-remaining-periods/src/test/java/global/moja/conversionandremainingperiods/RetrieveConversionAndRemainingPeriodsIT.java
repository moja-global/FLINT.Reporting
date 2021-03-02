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

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveConversionAndRemainingPeriodsIT.Initializer.class)
public class RetrieveConversionAndRemainingPeriodsIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod1;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod2;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod3;

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

        conversionAndRemainingPeriod2 =
                new ConversionAndRemainingPeriodBuilder()
                        .id(2L)
                        .previousLandCoverId(2L)
                        .currentLandCoverId(2L)
                        .conversionPeriod(2)
                        .remainingPeriod(2)
                        .version(1)
                        .build();

        conversionAndRemainingPeriod3 =
                new ConversionAndRemainingPeriodBuilder()
                        .id(3L)
                        .previousLandCoverId(3L)
                        .currentLandCoverId(3L)
                        .conversionPeriod(3)
                        .remainingPeriod(3)
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
    public void Given_ConversionAndRemainingPeriodRecordsExist_When_GetAllWithIdsFilter_Then_OnlyConversionAndRemainingPeriodRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/conversion_and_remaining_periods/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(conversionAndRemainingPeriod1.getId().toString(), conversionAndRemainingPeriod3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ConversionAndRemainingPeriod.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(conversionAndRemainingPeriod1.getId());
                            Assertions.assertThat(response.get(0).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(0).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(0).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod1.getConversionPeriod());
                            Assertions.assertThat(response.get(0).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod1.getRemainingPeriod());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(conversionAndRemainingPeriod3.getId());
                            Assertions.assertThat(response.get(1).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(1).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(1).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod3.getConversionPeriod());
                            Assertions.assertThat(response.get(1).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod3.getRemainingPeriod());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_ConversionAndRemainingPeriodRecordsExist_When_GetAllWithPreviousLandCoverIdFilter_Then_OnlyConversionAndRemainingPeriodRecordsWithTheSpecifiedPreviousLandCoverIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/conversion_and_remaining_periods/all")
                                .queryParam("previousLandCoverId", "{id1}")
                                .build(conversionAndRemainingPeriod3.getPreviousLandCoverId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ConversionAndRemainingPeriod.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(conversionAndRemainingPeriod3.getId());
                            Assertions.assertThat(response.get(0).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(0).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(0).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod3.getConversionPeriod());
                            Assertions.assertThat(response.get(0).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod3.getRemainingPeriod());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_ConversionAndRemainingPeriodRecordsExist_When_GetAllWithCurrentLandCoverIdFilter_Then_OnlyConversionAndRemainingPeriodRecordsWithTheSpecifiedCurrentLandCoverIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/conversion_and_remaining_periods/all")
                                .queryParam("currentLandCoverId", "{id1}")
                                .build(conversionAndRemainingPeriod3.getCurrentLandCoverId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ConversionAndRemainingPeriod.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(conversionAndRemainingPeriod3.getId());
                            Assertions.assertThat(response.get(0).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(0).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(0).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod3.getConversionPeriod());
                            Assertions.assertThat(response.get(0).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod3.getRemainingPeriod());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }

    @Test
    public void Given_ConversionAndRemainingPeriodRecordsExist_When_GetAllWithoutFilters_Then_AllConversionAndRemainingPeriodRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/conversion_and_remaining_periods/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ConversionAndRemainingPeriod.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(conversionAndRemainingPeriod1.getId());
                            Assertions.assertThat(response.get(0).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(0).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(0).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod1.getConversionPeriod());
                            Assertions.assertThat(response.get(0).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod1.getRemainingPeriod());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(conversionAndRemainingPeriod2.getId());
                            Assertions.assertThat(response.get(1).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod2.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(1).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod2.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(1).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod2.getConversionPeriod());
                            Assertions.assertThat(response.get(1).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod2.getRemainingPeriod());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(conversionAndRemainingPeriod3.getId());
                            Assertions.assertThat(response.get(2).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getPreviousLandCoverId());
                            Assertions.assertThat(response.get(2).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod3.getCurrentLandCoverId());
                            Assertions.assertThat(response.get(2).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod3.getConversionPeriod());
                            Assertions.assertThat(response.get(2).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod3.getRemainingPeriod());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
