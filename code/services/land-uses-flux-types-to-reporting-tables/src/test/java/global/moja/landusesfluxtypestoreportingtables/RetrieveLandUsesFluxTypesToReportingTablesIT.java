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

import java.util.Collections;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveLandUsesFluxTypesToReportingTablesIT.Initializer.class)
public class RetrieveLandUsesFluxTypesToReportingTablesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable1;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable2;
    static final LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable3;

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

        landUseFluxTypeToReportingTable2 =
                new LandUseFluxTypeToReportingTableBuilder()
                        .id(2L)
                        .landUseFluxTypeId(2L)
                        .emissionTypeId(2L)
                        .reportingTableId(null)
                        .version(1)
                        .build();

        landUseFluxTypeToReportingTable3 =
                new LandUseFluxTypeToReportingTableBuilder()
                        .id(3L)
                        .landUseFluxTypeId(3L)
                        .emissionTypeId(3L)
                        .reportingTableId(3L)
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
    public void Given_LandUseFluxTypeToReportingTableRecordsExist_When_GetAllWithIdsFilter_Then_OnlyLandUseFluxTypeToReportingTableRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(landUseFluxTypeToReportingTable1.getId().toString(), landUseFluxTypeToReportingTable3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxTypeToReportingTable1.getId());
                            Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getEmissionTypeId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(0L);
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseFluxTypeToReportingTable3.getId());
                            Assertions.assertThat(response.get(1).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(1).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getEmissionTypeId());
                            Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable3.getReportingTableId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeToReportingTableRecordsExist_When_GetAllWithLandUseFluxTypeIdFilter_Then_OnlyLandUseFluxTypeToReportingTableRecordsWithTheSpecifiedLandUseFluxTypeIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                                .queryParam("landUseFluxTypeId", "{id1}")
                                .build(landUseFluxTypeToReportingTable3.getLandUseFluxTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxTypeToReportingTable3.getId());
                            Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getEmissionTypeId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable3.getReportingTableId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeToReportingTableRecordsExist_When_GetAllWithEmissionTypeIdFilter_Then_OnlyLandUseFluxTypeToReportingTableRecordsWithTheSpecifiedEmissionTypeIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                                .queryParam("emissionTypeId", "{id1}")
                                .build(landUseFluxTypeToReportingTable3.getEmissionTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxTypeToReportingTable3.getId());
                            Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getEmissionTypeId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable3.getReportingTableId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeToReportingTableRecordsExist_When_GetAllWithReportingTableIdFilter_Then_OnlyLandUseFluxTypeToReportingTableRecordsWithTheSpecifiedReportingTableIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                                .queryParam("reportingTableId", "{id1}")
                                .build(landUseFluxTypeToReportingTable3.getReportingTableId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxTypeToReportingTable3.getId());
                            Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getEmissionTypeId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable3.getReportingTableId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_LandUseFluxTypeToReportingTableRecordsExist_When_GetAllWithoutFilters_Then_AllLandUseFluxTypeToReportingTableRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/land_uses_flux_types_to_reporting_tables/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LandUseFluxTypeToReportingTable.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(landUseFluxTypeToReportingTable1.getId());
                            Assertions.assertThat(response.get(0).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(0).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable1.getEmissionTypeId());
                            Assertions.assertThat(response.get(0).getReportingTableId()).isEqualTo(0L);
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(landUseFluxTypeToReportingTable2.getId());
                            Assertions.assertThat(response.get(1).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable2.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(1).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable2.getEmissionTypeId());
                            Assertions.assertThat(response.get(1).getReportingTableId()).isEqualTo(0L);
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(landUseFluxTypeToReportingTable3.getId());
                            Assertions.assertThat(response.get(2).getLandUseFluxTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getLandUseFluxTypeId());
                            Assertions.assertThat(response.get(2).getEmissionTypeId()).isEqualTo(landUseFluxTypeToReportingTable3.getEmissionTypeId());
                            Assertions.assertThat(response.get(2).getReportingTableId()).isEqualTo(landUseFluxTypeToReportingTable3.getReportingTableId());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
