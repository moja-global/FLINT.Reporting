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
@ContextConfiguration(initializers = UpdateLandUsesFluxTypesToReportingTablesIT.Initializer.class)
public class UpdateLandUsesFluxTypesToReportingTablesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable1;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable2;

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
                        .landUseFluxTypeId(10L)
                        .emissionTypeId(10L)
                        .reportingTableId(10L)
                        .version(1)
                        .build();

        landUseFluxTypeToReportingTable2 =
                new LandUseFluxTypeToReportingTableBuilder()
                        .id(2L)
                        .landUseFluxTypeId(20L)
                        .emissionTypeId(20L)
                        .reportingTableId(20L)
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



        LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables= new LandUseFluxTypeToReportingTable[]{landUseFluxTypeToReportingTable1, landUseFluxTypeToReportingTable2};

        webTestClient
                .put()
                .uri("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(landUsesFluxTypesToReportingTables), LandUseFluxTypeToReportingTable.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxTypeToReportingTable1.getId());
                    Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getLandUseFluxTypeId());
                    Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getEmissionTypeId());
                    Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable1.getReportingTableId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(landUseFluxTypeToReportingTable1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseFluxTypeToReportingTable2.getId());
                    Assertions.assertThat(response.get(1).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable2.getLandUseFluxTypeId());
                    Assertions.assertThat(response.get(1).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable2.getEmissionTypeId());
                    Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable2.getReportingTableId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(landUseFluxTypeToReportingTable2.getVersion() + 1);


                        }
                );
    }
}
