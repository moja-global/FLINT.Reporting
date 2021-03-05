/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules;

import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.util.builders.AccountabilityRuleBuilder;
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
@ContextConfiguration(initializers = UpdateAccountabilityRulesIT.Initializer.class)
public class UpdateAccountabilityRulesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final AccountabilityRule accountabilityRule1;
    static final AccountabilityRule accountabilityRule2;

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
                new AccountabilityRuleBuilder()
                        .id(1L)
                        .accountabilityTypeId(10L)
                        .parentPartyTypeId(10L)
                        .subsidiaryPartyTypeId(10L)
                        .version(1)
                        .build();

        accountabilityRule2 =
                new AccountabilityRuleBuilder()
                        .id(2L)
                        .accountabilityTypeId(20L)
                        .parentPartyTypeId(20L)
                        .subsidiaryPartyTypeId(20L)
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



        AccountabilityRule[] accountabilityRules= new AccountabilityRule[]{accountabilityRule1, accountabilityRule2};

        webTestClient
                .put()
                .uri("/api/v1/accountability_rules/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountabilityRules), AccountabilityRule.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityRule1.getId());
                    Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule1.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule1.getParentPartyTypeId());
                    Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule1.getSubsidiaryPartyTypeId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(accountabilityRule1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(accountabilityRule2.getId());
                    Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountabilityRule2.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(1).getParentPartyTypeId()).isEqualTo(accountabilityRule2.getParentPartyTypeId());
                    Assertions.assertThat(response.get(1).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule2.getSubsidiaryPartyTypeId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(accountabilityRule2.getVersion() + 1);


                        }
                );
    }
}
