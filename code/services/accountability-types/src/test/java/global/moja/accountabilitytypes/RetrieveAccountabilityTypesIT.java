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
@ContextConfiguration(initializers = RetrieveAccountabilityTypesIT.Initializer.class)
public class RetrieveAccountabilityTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final AccountabilityType accountabilityType1;
    static final AccountabilityType accountabilityType2;
    static final AccountabilityType accountabilityType3;

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
                        .name("First AccountabilityType")
                        .version(1)
                        .build();

        accountabilityType2 =
                new AccountabilityTypeBuilder()
                        .id(2L)
                        .name("Second AccountabilityType")
                        .version(1)
                        .build();

        accountabilityType3 =
                new AccountabilityTypeBuilder()
                        .id(3L)
                        .name("Third AccountabilityType")
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
    public void Given_AccountabilityTypeRecordsExist_When_GetAllWithIdsFilter_Then_OnlyAccountabilityTypeRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_types/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(accountabilityType1.getId().toString(), accountabilityType3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityType1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(accountabilityType1.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(accountabilityType3.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(accountabilityType3.getName());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }





    @Test
    public void Given_AccountabilityTypeRecordsExist_When_GetAllWithNameFilter_Then_OnlyAccountabilityTypeRecordsWithTheSpecifiedNameWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_types/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityType3.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(accountabilityType3.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_AccountabilityTypeRecordsExist_When_GetAllWithoutFilters_Then_AllAccountabilityTypeRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/accountability_types/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountabilityType.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(accountabilityType1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(accountabilityType1.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(accountabilityType2.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(accountabilityType2.getName());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(accountabilityType3.getId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(accountabilityType3.getName());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
