/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.insertion;

import global.moja.databases.configurations.DatabaseConfig;
import global.moja.databases.models.Database;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InsertDatabaseQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts a new Database record into the database
     *
     * @param database a bean containing the Database record details
     * @return the unique identifier of the newly inserted Database record
     */
    public Mono<Long> insertDatabase(Database database) {

        log.trace("Entering insertDatabase()");

        String query =
                "INSERT INTO database(" +
                        "label," +
                        "description," +
                        "url," +
                        "start_year," +
                        "end_year," +
                        "processed," +
                        "published," +
                        "archived) " +
                        "VALUES(?,?,?,?,?,?,?,?)";

        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(
                                        database.getLabel(),
                                        database.getDescription(),
                                        database.getUrl(),
                                        database.getStartYear(),
                                        database.getEndYear(),
                                        database.getProcessed(),
                                        database.getPublished(),
                                        database.getArchived())
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

}
