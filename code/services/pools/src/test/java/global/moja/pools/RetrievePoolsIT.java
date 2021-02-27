/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools;

import global.moja.pools.models.Pool;
import global.moja.pools.util.builders.PoolBuilder;
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
@ContextConfiguration(initializers = RetrievePoolsIT.Initializer.class)
public class RetrievePoolsIT {

    @Autowired
    WebTestClient webTestClient;

    static final PostgreSQLContainer postgreSQLContainer;
    static final Pool pool1;
    static final Pool pool2;
    static final Pool pool3;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("pools")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        pool1 =
                new PoolBuilder()
                        .id(0L)
                        .name("First Pool")
                        .description(null)
                        .version(1)
                        .build();

        pool2 =
                new PoolBuilder()
                        .id(1L)
                        .name("Second Pool")
                        .description(null)
                        .version(1)
                        .build();

        pool3 =
                new PoolBuilder()
                        .id(2L)
                        .name("Third Pool")
                        .description("Third Pool Description")
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
    public void Given_PoolRecordsExist_When_GetAllWithIdsFilter_Then_OnlyPoolRecordsWithTheSpecifiedIdsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/pools/all")
                                .queryParam("ids", "{id1}", "{id2}")
                                .build(pool1.getId().toString(), pool3.getId().toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Pool.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(pool1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(pool1.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isNull();
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(pool3.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(pool3.getName());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(pool3.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);


                        }
                );
    }

    @Test
    public void Given_PoolRecordsExist_When_GetAllWithNameFilter_Then_OnlyPoolRecordsWithTheSpecifiedNameWillBeReturned() {


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/pools/all")
                                .queryParam("name", "{id1}")
                                .build("Thi"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Pool.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(pool3.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(pool3.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isEqualTo(pool3.getDescription());
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                        }
                );
    }


    @Test
    public void Given_PoolRecordsExist_When_GetAllWithoutFilters_Then_AllPoolRecordsWillBeReturned() {

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/pools/all")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Pool.class)
                .value(response -> {

                            Collections.sort(response);

                            Assertions.assertThat(response.get(0).getId()).isEqualTo(pool1.getId());
                            Assertions.assertThat(response.get(0).getName()).isEqualTo(pool1.getName());
                            Assertions.assertThat(response.get(0).getDescription()).isNull();
                            Assertions.assertThat(response.get(0).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(1).getId()).isEqualTo(pool2.getId());
                            Assertions.assertThat(response.get(1).getName()).isEqualTo(pool2.getName());
                            Assertions.assertThat(response.get(1).getDescription()).isEqualTo(pool2.getDescription());
                            Assertions.assertThat(response.get(1).getVersion()).isEqualTo(1);

                            Assertions.assertThat(response.get(2).getId()).isEqualTo(pool3.getId());
                            Assertions.assertThat(response.get(2).getName()).isEqualTo(pool3.getName());
                            Assertions.assertThat(response.get(2).getDescription()).isEqualTo(pool3.getDescription());
                            Assertions.assertThat(response.get(2).getVersion()).isEqualTo(1);


                        }
                );
    }

}
