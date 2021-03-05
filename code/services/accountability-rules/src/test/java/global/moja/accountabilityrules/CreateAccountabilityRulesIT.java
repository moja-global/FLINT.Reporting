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
@ContextConfiguration(initializers = CreateAccountabilityRulesIT.Initializer.class)
public class CreateAccountabilityRulesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final AccountabilityRule accountabilityRule4;
    static final AccountabilityRule accountabilityRule5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("accountabilityRules")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        accountabilityRule4 =
                new AccountabilityRuleBuilder()
                        .accountabilityTypeId(4L)
                        .parentPartyTypeId(4L)
                        .subsidiaryPartyTypeId(4L)
                        .build();

        accountabilityRule5 =
                new AccountabilityRuleBuilder()
                        .accountabilityTypeId(5L)
                        .parentPartyTypeId(5L)
                        .subsidiaryPartyTypeId(5L)
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
    public void Given_AccountabilityRuleDetailsList_When_PostAll_Then_AccountabilityRuleRecordsWillBeCreatedAndReturned() {

        AccountabilityRule[] accountabilityRules= new AccountabilityRule[]{accountabilityRule4, accountabilityRule5};

        webTestClient
                .post()
                .uri("/api/v1/accountability_rules/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountabilityRules), AccountabilityRule.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(AccountabilityRule.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountabilityRule4.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(accountabilityRule4.getParentPartyTypeId());
                    Assertions.assertThat(response.get(0).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule4.getSubsidiaryPartyTypeId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountabilityRule5.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(1).getParentPartyTypeId()).isEqualTo(accountabilityRule5.getParentPartyTypeId());
                    Assertions.assertThat(response.get(1).getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule5.getSubsidiaryPartyTypeId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
