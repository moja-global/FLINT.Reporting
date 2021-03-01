/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables;

import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
import global.moja.landusesfluxtypestoreportingtables.util.builders.LandUseFluxTypeToReportingTableBuilder;
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
@ContextConfiguration(initializers = RetrieveLandUseFluxTypeToReportingTableIT.Initializer.class)
public class RetrieveLandUseFluxTypeToReportingTableIT {

    @Autowired
    WebTestClient webTestClient;


    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("landUsesFluxTypesToReportingTables")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        landUseFluxTypeToReportingTable1 =
                new LandUseFluxTypeToReportingTableBuilder()
                        .id(1L)
                        .landUseFluxTypeId(1L)
                        .emissionTypeId(1L)
                        .reportingTableId(null)
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
    public void Given_LandUseFluxTypeToReportingTableRecordExists_When_GetWithIdParameter_Then_TheLandUseFluxTypeToReportingTableRecordWithThatIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types_to_reporting_tables/ids/{id}")
                                .build(Long.toString(landUseFluxTypeToReportingTable1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LandUseFluxTypeToReportingTable.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(landUseFluxTypeToReportingTable1.getId());
                            Assertions.assertThat(response.getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getLandUseFluxTypeId());
                            Assertions.assertThat(response.getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getEmissionTypeId());
                            Assertions.assertThat(response.getReportingTableId()).isEqualTo(0L);
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
