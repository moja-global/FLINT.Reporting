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
@ContextConfiguration(initializers = CreateLandUsesFluxTypesToReportingTablesIT.Initializer.class)
public class CreateLandUsesFluxTypesToReportingTablesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable4;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("landUsesFluxTypesToReportingTables")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        landUseFluxTypeToReportingTable4 =
                new LandUseFluxTypeToReportingTableBuilder()
                        .id(null)
                        .landUseFluxTypeId(4L)
                        .emissionTypeId(4L)
                        .reportingTableId(null)
                        .version(null)
                        .build();

        landUseFluxTypeToReportingTable5 =
                new LandUseFluxTypeToReportingTableBuilder()
                        .id(null)
                        .landUseFluxTypeId(5L)
                        .emissionTypeId(5L)
                        .reportingTableId(5L)
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
    public void Given_LandUseFluxTypeToReportingTableDetailsList_When_PostAll_Then_LandUseFluxTypeToReportingTableRecordsWillBeCreatedAndReturned() {

        LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables= new LandUseFluxTypeToReportingTable[]{landUseFluxTypeToReportingTable4, landUseFluxTypeToReportingTable5};

        webTestClient
                .post()
                .uri("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(landUsesFluxTypesToReportingTables), LandUseFluxTypeToReportingTable.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable4.getLandUseFluxTypeId());
                    Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable4.getEmissionTypeId());
                    Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(0L);
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable5.getLandUseFluxTypeId());
                    Assertions.assertThat(response.get(1).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable5.getEmissionTypeId());
                    Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable5.getReportingTableId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
