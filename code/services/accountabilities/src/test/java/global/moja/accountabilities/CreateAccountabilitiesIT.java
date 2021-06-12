/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities;

import global.moja.accountabilities.models.Accountability;
import global.moja.accountabilities.util.builders.AccountabilityBuilder;
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
@ContextConfiguration(initializers = CreateAccountabilitiesIT.Initializer.class)
public class CreateAccountabilitiesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Accountability accountability5;
    static final Accountability accountability6;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("accountabilities")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        accountability5 =
                new AccountabilityBuilder()
                        .accountabilityTypeId(5L)
                        .parentPartyId(5L)
                        .subsidiaryPartyId(5L)
                        .build();

        accountability6 =
                new AccountabilityBuilder()
                        .accountabilityTypeId(6L)
                        .parentPartyId(6L)
                        .subsidiaryPartyId(6L)
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
    public void Given_AccountabilityDetailsList_When_PostAll_Then_AccountabilityRecordsWillBeCreatedAndReturned() {

        Accountability[] accountabilities= new Accountability[]{accountability5, accountability6};

        webTestClient
                .post()
                .uri("/api/v1/accountabilities/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountabilities), Accountability.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Accountability.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability5.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability5.getParentPartyId());
                    Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability5.getSubsidiaryPartyId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(6L);
                    Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountability6.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(1).getParentPartyId()).isEqualTo(accountability6.getParentPartyId());
                    Assertions.assertThat(response.get(1).getSubsidiaryPartyId()).isEqualTo(accountability6.getSubsidiaryPartyId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                });
    }
}
