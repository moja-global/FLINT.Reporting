/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.configurations;

import global.moja.dates.util.endpoints.EndpointsUtil;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
public class DatabaseConfig {


    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    EndpointsUtil endpointsUtil;

    private final Map<Long,Database> databases = new HashMap<>();


    public final Mono<Database> getDatabase(Long id) {

        if(databases.get(id) != null) {
            return
                    Mono.just(databases.get(id));
        } else {
            return
                    endpointsUtil.retrieveDatabaseById(id)
                            .map(d -> Database
                                    .nonBlocking()
                                    .url(d.getUrl())
                                    .user(this.username)
                                    .password(this.password)
                                    .healthCheck(DatabaseType.POSTGRES)
                                    .idleTimeBeforeHealthCheck(5, TimeUnit.SECONDS)
                                    .maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                                    .build())
                            .doOnNext(d -> {
                                databases.put(id,d);
                            });
        }


    }

}
