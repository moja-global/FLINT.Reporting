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

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = RetrieveAccountabilityIT.Initializer.class)
public class RetrieveAccountabilityIT {

    @Autowired
    WebTestClient webTestClient;


    static final PostgreSQLContainer postgreSQLContainer;
    static final Accountability accountability1;

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
    public void Given_AccountabilityRecordExists_When_GetWithIdParameter_Then_TheAccountabilityRecordWithThatIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountabilities/ids/{id}")
                                .build(Long.toString(accountability1.getId())))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Accountability.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(accountability1.getId());
                            Assertions.assertThat(response.getAccountabilityTypeId()).isEqualTo(accountability1.getAccountabilityTypeId());
                            Assertions.assertThat(response.getAccountabilityRuleId()).isEqualTo(accountability1.getAccountabilityRuleId());
                            Assertions.assertThat(response.getParentPartyId()).isEqualTo(accountability1.getParentPartyId());
                            Assertions.assertThat(response.getSubsidiaryPartyId()).isEqualTo(accountability1.getSubsidiaryPartyId());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
