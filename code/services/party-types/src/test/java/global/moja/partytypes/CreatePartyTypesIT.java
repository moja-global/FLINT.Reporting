/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes;

import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.util.builders.PartyTypeBuilder;
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
@ContextConfiguration(initializers = CreatePartyTypesIT.Initializer.class)
public class CreatePartyTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final PartyType partyType4;
    static final PartyType partyType5;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("partyTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        partyType4 =
                new PartyTypeBuilder()
                        .parentPartyTypeId(4L)
                        .name("Fourth PartyType")
                        .build();

        partyType5 =
                new PartyTypeBuilder()
                        .parentPartyTypeId(5L)
                        .name("Fifth PartyType")
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
    public void Given_PartyTypeDetailsList_When_PostAll_Then_PartyTypeRecordsWillBeCreatedAndReturned() {

        PartyType[] partyTypes= new PartyType[]{partyType4, partyType5};

        webTestClient
                .post()
                .uri("/api/v1/party_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(partyTypes), PartyType.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(PartyType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(4L);
                    Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(partyType4.getParentPartyTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(partyType4.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(5L);
                    Assertions.assertThat(response.get(1).getParentPartyTypeId()).isEqualTo(partyType5.getParentPartyTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(partyType5.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);
                });
    }
}
