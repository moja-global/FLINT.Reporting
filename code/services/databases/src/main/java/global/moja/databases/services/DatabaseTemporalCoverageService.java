
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.databases.services;


import global.moja.databases.daos.DatabaseTemporalScale;
import global.moja.databases.models.Date;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class DatabaseTemporalCoverageService {

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    /**
     * Retrieves the database's Temporal Scale given its url
     *
     * @param url the url of the database
     * @return the database's validation result
     */
    public Mono<DatabaseTemporalScale> retrieveDatabaseTemporalScale(String url) {

        log.trace("Entering validateDatabase()");

        // Get a database connection if possible
        final Database database = isConnectable(url)? getDatabase(url) : null;

        // Create a new Date Range bean
        final DatabaseTemporalScale databaseTemporalScale = DatabaseTemporalScale.builder().build();

        // Populate and return the Date Range bean
        return getMinimumYear(database)
                .doOnNext(year -> {
                    log.info("Minimum year = {}", year);
                    databaseTemporalScale.setMin(year);
                })
                .flatMap(year -> getMaximumYear(database))
                .doOnNext(year -> {
                    log.info("Maximum year = {}", year);
                    databaseTemporalScale.setMax(year);
                    if(database != null) {
                        database.close();
                    }
                })
                .map(year -> databaseTemporalScale);

    }

    private boolean isConnectable(String url) {

        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            connection.close();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    private Database getDatabase(String url) {

        return Database
                .nonBlocking()
                .url(url)
                .user(this.username)
                .password(this.password)
                .healthCheck(DatabaseType.POSTGRES)
                .idleTimeBeforeHealthCheck(5, TimeUnit.SECONDS)
                .maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                .build();
    }

    private Mono<Integer> getMinimumYear(Database database) {

        // Get the minimum year
        log.info("Getting the minimum year");

        if(database == null) {
            return Mono.just(-1);
        }

        String query = "SELECT * FROM date_dimension ORDER BY year ASC LIMIT 2";

        return Flux.from(database
                .select(query)
                .get(rs ->
                        Date.builder()
                                .id(rs.getLong("date_dimension_id_pk"))
                                .year(rs.getInt("year"))
                                .build())
                .onErrorResumeNext(Flowable.just(new Date(), new Date())))
                .filter(date -> date.getId() != null)
                .filter(date -> date.getYear() != null)
                .collectList()
                .map(dates -> {
                    if(dates.size() == 2) {
                        return dates.get(0).getYear() == 0 ? dates.get(1).getYear(): dates.get(0).getYear();
                    } else {
                        return dates.get(0).getYear();
                    }
                });
    }

    private Mono<Integer> getMaximumYear(Database database) {

        // Get the maximum year
        log.info("Getting the maximum year");

        if(database == null) {
            return Mono.just(-1);
        }

        String query = "SELECT max(year) as year FROM date_dimension";

        return Mono.from(database
                .select(query)
                .get(rs -> rs.getInt("year"))
                .onErrorResumeNext(Flowable.just(-1)));


    }


}
