/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules;

import global.moja.accountabilityrules.models.AccountabilityRule;
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
@ContextConfiguration(initializers = RetrieveAccountabilityRulesIT.Initializer.class)
public class RetrieveAccountabilityRulesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final AccountabilityRule accountabilityRule1;
    static final AccountabilityRule accountabilityRule2;
    static final AccountabilityRule accountabilityRule3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("accountabilityRules")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        accountabilityRule1 =
                AccountabilityRule.builder()
                        .id(1L)
                        .accountabilityTypeId(1L)
                        .parentPartyTypeId(1L)
                        .subsidiaryPartyTypeId(1L)
                        .version(1)
                        .build();

        accountabilityRule2 =
                AccountabilityRule.builder()
                        .id(2L)
                        .accountabilityTypeId(2L)
                        .parentPartyTypeId(2L)
                        .subsidiaryPartyTypeId(2L)
                        .version(1)
                        .build();

        accountabilityRule3 =
                AccountabilityRule.builder()
                        .id(3L)
                        .accountabilityTypeId(3L)
                        .parentPartyTypeId(3L)
                        .subsidiaryPartyTypeId(3L)
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
    public void Given_AccountabilityRuleRecordsExist_When_GetAllWithIdsFilter_Then_OnlyAccountabilityRuleRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_rules/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(accountabilityRule1.getId().toString(), accountabilityRule3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule1.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule1.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule1.getParentPartyTypeId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule1.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountabilityRule3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(1).getParentPartyTypeId()).isEqualTo(accountabilityRule3.getParentPartyTypeId());
                            Assertions.assertThat(response.get(1).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule3.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_AccountabilityRuleRecordsExist_When_GetAllWithAccountabilityRuleTypeIdFilter_Then_OnlyAccountabilityRuleRecordsWithTheSpecifiedAccountabilityRuleTypeIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_rules/all")
                                .queryParam("accountabilityTypeId", "{id1}")
                                .build(accountabilityRule3.getAccountabilityTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule3.getParentPartyTypeId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule3.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_AccountabilityRuleRecordsExist_When_GetAllWithParentPartyIdFilter_Then_OnlyAccountabilityRuleRecordsWithTheSpecifiedParentPartyTypeIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_rules/all")
                                .queryParam("parentPartyTypeId", "{id1}")
                                .build(accountabilityRule3.getParentPartyTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule3.getParentPartyTypeId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule3.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_AccountabilityRuleRecordsExist_When_GetAllWithSubsidiaryPartyIdFilter_Then_OnlyAccountabilityRuleRecordsWithTheSpecifiedSubsidiaryPartyTypeIdWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_rules/all")
                                .queryParam("subsidiaryPartyTypeId", "{id1}")
                                .build(accountabilityRule3.getSubsidiaryPartyTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule3.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule3.getParentPartyTypeId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule3.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_AccountabilityRuleRecordsExist_When_GetAllWithoutFilters_Then_AllAccountabilityRuleRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_rules/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule1.getId());
                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule1.getId());
                            Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule1.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule1.getParentPartyTypeId());
                            Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule1.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(accountabilityRule2.getId());
                            Assertions.assertThat(response.get(1).getId()).isEqualTo(accountabilityRule2.getId());
                            Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountabilityRule2.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(1).getParentPartyTypeId()).isEqualTo(accountabilityRule2.getParentPartyTypeId());
                            Assertions.assertThat(response.get(1).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule2.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(accountabilityRule3.getId());
                            Assertions.assertThat(response.get(2).getId()).isEqualTo(accountabilityRule3.getId());
                            Assertions.assertThat(response.get(2).getAccountabilityTypeId()).isEqualTo(accountabilityRule3.getAccountabilityTypeId());
                            Assertions.assertThat(response.get(2).getParentPartyTypeId()).isEqualTo(accountabilityRule3.getParentPartyTypeId());
                            Assertions.assertThat(response.get(2).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule3.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
