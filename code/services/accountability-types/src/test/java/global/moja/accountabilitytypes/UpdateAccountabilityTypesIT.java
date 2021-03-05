/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes;

import global.moja.accountabilitytypes.models.AccountabilityType;
import global.moja.accountabilitytypes.util.builders.AccountabilityTypeBuilder;
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
@ContextConfiguration(initializers = UpdateAccountabilityTypesIT.Initializer.class)
public class UpdateAccountabilityTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final AccountabilityType accountabilityType1;
    static final AccountabilityType accountabilityType2;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("accountabilityTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        accountabilityType1 =
                new AccountabilityTypeBuilder()
                        .id(1L)
                        .name("FIRST ACCOUNTABILITY TYPE")
                        .version(1)
                        .build();

        accountabilityType2 =
                new AccountabilityTypeBuilder()
                        .id(2L)
                        .name("SECOND INDICATOR")
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



        AccountabilityType[] accountabilityTypes= new AccountabilityType[]{accountabilityType1, accountabilityType2};

        webTestClient
                .put()
                .uri("/api/v1/accountability_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountabilityTypes), AccountabilityType.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AccountabilityType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityType1.getId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(accountabilityType1.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(accountabilityType1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(accountabilityType2.getId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(accountabilityType2.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(accountabilityType2.getVersion() + 1);


                        }
                );
    }
}
