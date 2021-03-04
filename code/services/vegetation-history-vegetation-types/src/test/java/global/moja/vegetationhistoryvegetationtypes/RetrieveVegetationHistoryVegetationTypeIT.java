/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes;

import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;
import global.moja.vegetationhistoryvegetationtypes.util.builders.DatabaseBuilder;
import global.moja.vegetationhistoryvegetationtypes.util.builders.VegetationHistoryVegetationTypeBuilder;
import global.moja.vegetationhistoryvegetationtypes.util.endpoints.EndpointsUtil;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
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
@ContextConfiguration(initializers = RetrieveVegetationHistoryVegetationTypeIT.Initializer.class)
public class RetrieveVegetationHistoryVegetationTypeIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    EndpointsUtil endpointsUtil;

    static final PostgreSQLContainer postgreSQLContainer;
    static final VegetationHistoryVegetationType vegetationHistoryVegetationType1;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("vegetationHistoryVegetationTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        vegetationHistoryVegetationType1 =
                new VegetationHistoryVegetationTypeBuilder()
                        .id(1L)
                        .vegetationHistoryId(1L)
                        .vegetationTypeId(1L)
                        .itemNumber(1L)
                        .year(1991)
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
    public void Given_VegetationHistoryVegetationTypeRecordExists_When_GetWithIdParameter_Then_TheVegetationHistoryVegetationTypeRecordWithThatIdWillBeReturned() {

        Mockito
                .when(endpointsUtil.retrieveDatabaseById(1L))
                .thenReturn(
                        Mono.just(
                                new DatabaseBuilder()
                                        .id(null)
                                        .label("First Database")
                                        .description(null)
                                        .url(postgreSQLContainer.getJdbcUrl())
                                        .startYear(1984)
                                        .endYear(2014)
                                        .processed(false)
                                        .published(false)
                                        .archived(false)
                                        .build()));

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/vegetation_history_vegetation_types/databases/1/ids/{id}")
                                .build(Long.toString(vegetationHistoryVegetationType1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VegetationHistoryVegetationType.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(vegetationHistoryVegetationType1.getId());
                            Assertions.assertThat(response.getVegetationHistoryId()).isEqualTo(vegetationHistoryVegetationType1.getVegetationHistoryId());
                            Assertions.assertThat(response.getVegetationTypeId()).isEqualTo(vegetationHistoryVegetationType1.getVegetationTypeId());
                            Assertions.assertThat(response.getItemNumber()).isEqualTo(vegetationHistoryVegetationType1.getItemNumber());
                            Assertions.assertThat(response.getYear()).isEqualTo(vegetationHistoryVegetationType1.getYear());
                        }
                );
    }
}
