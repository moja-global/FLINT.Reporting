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
@ContextConfiguration(initializers = UpdatePartyTypesIT.Initializer.class)
public class UpdatePartyTypesIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final PartyType partyType1;
    static final PartyType partyType2;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("partyTypes")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        partyType1 =
                new PartyTypeBuilder()
                        .id(1L)
                        .parentPartyTypeId(10L)
                        .name("FIRST PARTY TYPE")
                        .version(1)
                        .build();

        partyType2 =
                new PartyTypeBuilder()
                        .id(2L)
                        .parentPartyTypeId(20L)
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



        PartyType[] partyTypes= new PartyType[]{partyType1, partyType2};

        webTestClient
                .put()
                .uri("/api/v1/party_types/all")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(partyTypes), PartyType.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PartyType.class)
                .value(response -> {

                    Collections.sort(response);

                    Assertions.assertThat(response.get(0).getId()).isEqualTo(partyType1.getId());
                    Assertions.assertThat(response.get(0).getParentPartyTypeId()).isEqualTo(partyType1.getParentPartyTypeId());
                    Assertions.assertThat(response.get(0).getName()).isEqualTo(partyType1.getName());
                    Assertions.assertThat(response.get(0).getVersion()).isEqualTo(partyType1.getVersion() + 1);

                    Assertions.assertThat(response.get(1).getId()).isEqualTo(partyType2.getId());
                    Assertions.assertThat(response.get(1).getParentPartyTypeId()).isEqualTo(partyType2.getParentPartyTypeId());
                    Assertions.assertThat(response.get(1).getName()).isEqualTo(partyType2.getName());
                    Assertions.assertThat(response.get(1).getVersion()).isEqualTo(partyType2.getVersion() + 1);


                        }
                );
    }
}
