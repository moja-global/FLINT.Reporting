/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties;

import global.moja.parties.models.Party;
import global.moja.parties.util.builders.PartyBuilder;
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
@ContextConfiguration(initializers = RetrievePartiesIT.Initializer.class)
public class RetrievePartiesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Party party1;
    static final Party party2;
    static final Party party3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("parties")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        party1 =
                new PartyBuilder()
                        .id(0L)
                        .partyTypeId(1L)
                        .name("First Party")
                        .version(1)
                        .build();

        party2 =
                new PartyBuilder()
                        .id(1L)
                        .partyTypeId(2L)
                        .name("Second Party")
                        .version(1)
                        .build();

        party3 =
                new PartyBuilder()
                        .id(2L)
                        .partyTypeId(3L)
                        .name("Third Party")
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
    public void Given_PartyRecordsExist_When_GetAllWithIdsFilter_Then_OnlyPartyRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/parties/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(party1.getId().toString(), party3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Party.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(party1.getId());
                            Assertions.assertThat(response.get(0).getPartyTypeId()).isEqualTo(party1.getPartyTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(party1.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(party3.getId());
                            Assertions.assertThat(response.get(1).getPartyTypeId()).isEqualTo(party3.getPartyTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(party3.getName());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }


    @Test
    public void Given_PartyRecordsExist_When_GetAllWithPartyTypeIdFilter_Then_OnlyPartyRecordsWithTheSpecifiedPartyTypeIdWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/parties/all")
                                .queryParam("partyTypeId", "{id1}")
                                .build(party3.getPartyTypeId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Party.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(party3.getId());
                            Assertions.assertThat(response.get(0).getPartyTypeId()).isEqualTo(party3.getPartyTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(party3.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }







    @Test
    public void Given_PartyRecordsExist_When_GetAllWithNameFilter_Then_OnlyPartyRecordsWithTheSpecifiedNameWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/parties/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Party.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(party3.getId());
                            Assertions.assertThat(response.get(0).getPartyTypeId()).isEqualTo(party3.getPartyTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(party3.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_PartyRecordsExist_When_GetAllWithoutFilters_Then_AllPartyRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/parties/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Party.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(party1.getId());
                            Assertions.assertThat(response.get(0).getPartyTypeId()).isEqualTo(party1.getPartyTypeId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(party1.getName());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(party2.getId());
                            Assertions.assertThat(response.get(1).getPartyTypeId()).isEqualTo(party2.getPartyTypeId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(party2.getName());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(party3.getId());
                            Assertions.assertThat(response.get(2).getPartyTypeId()).isEqualTo(party3.getPartyTypeId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(party3.getName());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
