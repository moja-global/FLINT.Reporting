/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.configurations;

import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Database database;

    @PostConstruct
    private void init() {
        this.database =
                Database
                        .nonBlocking()
                        .url(this.url)
                        .user(this.username)
                        .password(this.password)
                        .healthCheck(DatabaseType.POSTGRES)
                        .idleTimeBeforeHealthCheck(5, TimeUnit.SECONDS)
                        .maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                        .build();
    }

    public DatabaseConfig url(String url){
        this.url = url;
        return this;
    }

    public DatabaseConfig username(String username){
        this.username = username;
        return this;
    }

    public DatabaseConfig password(String password){
        this.password = password;
        return this;
    }

    public DatabaseConfig build(){
        this.init();
        return this;
    }

    public final Database getDatabase() {
        return database;
    }

}
