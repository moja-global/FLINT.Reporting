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
@ContextConfiguration(initializers = UpdateAccountabilitiesIT.Initializer.class)
public class UpdateAccountabilitiesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Accountability accountability1;
    static final Accountability accountability2;

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
                        .accountabilityTypeId(10L)
                        .accountabilityRuleId(10L)
                        .parentPartyId(10L)
                        .subsidiaryPartyId(10L)
                        .version(1)
                        .build();

        accountability2 =
                Accountability.builder()
                        .id(2L)
                        .accountabilityTypeId(20L)
                        .accountabilityRuleId(20L)
                        .parentPartyId(20L)
                        .subsidiaryPartyId(20L)
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



        Accountability[] accountabilities= new Accountability[]{accountability1, accountability2};

        webTestClient
                .put()
                .uri("/api/v1/accountabilities/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountabilities), Accountability.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Accountability.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(accountability1.getId());
                    Assertions.assertThat(response.get(0).getAccountabilityTypeId()).isEqualTo(accountability1.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(0).getAccountabilityRuleId()).isEqualTo(accountability1.getAccountabilityRuleId());
                    Assertions.assertThat(response.get(0).getParentPartyId()).isEqualTo(accountability1.getParentPartyId());
                    Assertions.assertThat(response.get(0).getSubsidiaryPartyId()).isEqualTo(accountability1.getSubsidiaryPartyId());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(accountability1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(accountability2.getId());
                    Assertions.assertThat(response.get(1).getAccountabilityTypeId()).isEqualTo(accountability2.getAccountabilityTypeId());
                    Assertions.assertThat(response.get(1).getAccountabilityRuleId()).isEqualTo(accountability2.getAccountabilityRuleId());
                    Assertions.assertThat(response.get(1).getParentPartyId()).isEqualTo(accountability2.getParentPartyId());
                    Assertions.assertThat(response.get(1).getSubsidiaryPartyId()).isEqualTo(accountability2.getSubsidiaryPartyId());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(accountability2.getVersion() + 1);




                        }
                );
    }
}
