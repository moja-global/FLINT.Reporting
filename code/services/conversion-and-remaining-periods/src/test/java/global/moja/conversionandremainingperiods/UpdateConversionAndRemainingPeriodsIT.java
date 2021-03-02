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
@ContextConfiguration(initializers = UpdateConversionAndRemainingPeriodsIT.Initializer.class)
public class UpdateConversionAndRemainingPeriodsIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod1;
    static final ConversionAndRemainingPeriod conversionAndRemainingPeriod2;

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
                        .previousLandCoverId(10L)
                        .currentLandCoverId(10L)
                        .conversionPeriod(10)
                        .remainingPeriod(10)
                        .version(1)
                        .build();

        conversionAndRemainingPeriod2 =
                new ConversionAndRemainingPeriodBuilder()
                        .id(2L)
                        .previousLandCoverId(20L)
                        .currentLandCoverId(20L)
                        .conversionPeriod(20)
                        .remainingPeriod(20)
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



        ConversionAndRemainingPeriod[] conversionAndRemainingPeriods= new ConversionAndRemainingPeriod[]{conversionAndRemainingPeriod1, conversionAndRemainingPeriod2};

        webTestClient
                .put()
                .uri("/api/v1/conversion_and_remaining_periods/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(conversionAndRemainingPeriods), ConversionAndRemainingPeriod.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ConversionAndRemainingPeriod.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(conversionAndRemainingPeriod1.getId());
                    Assertions.assertThat(response.get(0).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getPreviousLandCoverId());
                    Assertions.assertThat(response.get(0).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod1.getCurrentLandCoverId());
                    Assertions.assertThat(response.get(0).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod1.getConversionPeriod());
                    Assertions.assertThat(response.get(0).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod1.getRemainingPeriod());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(conversionAndRemainingPeriod1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(conversionAndRemainingPeriod2.getId());
                    Assertions.assertThat(response.get(1).getPreviousLandCoverId()).isEqualTo(conversionAndRemainingPeriod2.getPreviousLandCoverId());
                    Assertions.assertThat(response.get(1).getCurrentLandCoverId()).isEqualTo(conversionAndRemainingPeriod2.getCurrentLandCoverId());
                    Assertions.assertThat(response.get(1).getConversionPeriod()).isEqualTo(conversionAndRemainingPeriod2.getConversionPeriod());
                    Assertions.assertThat(response.get(1).getRemainingPeriod()).isEqualTo(conversionAndRemainingPeriod2.getRemainingPeriod());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(conversionAndRemainingPeriod2.getVersion() + 1);


                        }
                );
    }
}
