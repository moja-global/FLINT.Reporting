/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities;

import global.moja.accountabilities.models.Accountability;
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
@ContextConfiguration(initializers = RetrieveAccountabilitiesIT.Initializer.class)
public class RetrieveAccountabilitiesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Accountability accountability1;
    static final Accountability accountability2;
    static final Accountability accountability3;
    static final Accountability accountability4;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("accountabilities")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        accountability1 =
                Accountability.builder()
                        .id(1L)
                        .accountabilityTypeId(1L)
                        .accountabilityRuleId(1L)
                        .parentPartyId(1L)
                        .subsidiaryPartyId(1L)
                        .version(1)
                        .build();

        accountability2 =
                Accountability.builder()
                        .id(2L)
                        .accountabilityTypeId(2L)
                        .accountabilityRuleId(2L)
                        .parentPartyId(2L)
                        .subsidiaryPartyId(2L)
                        .version(1)
                        .build();

        accountability3 =
                Accountability.builder()
                        .id(3L)
                        .accountabilityTypeId(3L)
                        .accountabilityRuleId(3L)
                        .parentPartyId(3L)
                        .subsidiaryPartyId(3L)
                        .version(1)
                        .build();

        accountability4 =
                Accountability.builder()
                        .id(4L)
                        .accountabilityTypeId(4L)
                        .accountabilityRuleId(4L)
                        .parentPartyId(0L)
                        .subsidiaryPartyId(4L)
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
    public void Given_AccountabilityRecordsExist_When_GetAllWithIdsFilter_Then_OnlyAccountabilityRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(accountability1.getId().toString(), accountability3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability1.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability1.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability1.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability1.getParentPartyId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability1.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(accountability3.getId());
                            Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountability3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(1).getAccountabilityRuleId()).isEqualTo(accountability3.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(1).getParentPartyId()).isEqualTo(accountability3.getParentPartyId());
                            Assertions.assertThat(response.get(1).getSubsidiaryPartyId()).isEqualTo(accountability3.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_AccountabilityRecordsExist_When_GetAllWithAccountabilityTypeIdFilter_Then_OnlyAccountabilityRecordsWithTheSpecifiedAccountabilityTypeIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/all")
                                .queryParam("accountabilityTypeId", "{id1}")
                                .build(accountability3.getAccountabilityTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability3.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability3.getParentPartyId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability3.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_AccountabilityRecordsExist_When_GetAllWithAccountabilityRuleIdFilter_Then_OnlyAccountabilityRecordsWithTheSpecifiedAccountabilityRuleIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/all")
                                .queryParam("accountabilityRuleId", "{id1}")
                                .build(accountability3.getAccountabilityRuleId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability3.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability3.getParentPartyId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability3.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_AccountabilityRecordsExist_When_GetAllWithParentPartyIdFilter_Then_OnlyAccountabilityRecordsWithTheSpecifiedParentPartyIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/all")
                                .queryParam("parentPartyId", "{id1}")
                                .build(accountability3.getParentPartyId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability3.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability3.getParentPartyId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability3.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );

    }


    @Test
    public void Given_AccountabilityRecordsExist_When_GetAllWithZeroAsTheParentPartyIdFilter_Then_OnlyAccountabilityRecordsWithNullParentPartyIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/all")
                                .queryParam("parentPartyId", "0")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability4.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability4.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability4.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability4.getParentPartyId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability4.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );

    }


    @Test
    public void Given_AccountabilityRecordsExist_When_GetAllWithSubsidiaryPartyIdFilter_Then_OnlyAccountabilityRecordsWithTheSpecifiedSubsidiaryPartyIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/all")
                                .queryParam("subsidiaryPartyId", "{id1}")
                                .build(accountability3.getSubsidiaryPartyId()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability3.getAccountabilityRuleId());
                            Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability3.getParentPartyId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability3.getSubsidiaryPartyId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


}
