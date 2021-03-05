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
@ContextConfiguration(initializers = UpdatePartiesIT.Initializer.class)
public class UpdatePartiesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Party party1;
    static final Party party2;

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
                        .partyTypeId(10L)
                        .name("FIRST INDICATOR")
                        .version(1)
                        .build();

        party2 =
                new PartyBuilder()
                        .id(1L)
                        .partyTypeId(20L)
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



        Party[] parties= new Party[]{party1, party2};

        webTestClient
                .put()
                .uri("/api/v1/parties/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(parties), Party.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Party.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(party1.getId());
                    Assertions.assertThat(response.get(0).getPartyTypeId()).isEqualTo(party1.getPartyTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(party1.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(party1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(party2.getId());
                    Assertions.assertThat(response.get(1).getPartyTypeId()).isEqualTo(party2.getPartyTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(party2.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(party2.getVersion() + 1);


                        }
                );
    }
}
