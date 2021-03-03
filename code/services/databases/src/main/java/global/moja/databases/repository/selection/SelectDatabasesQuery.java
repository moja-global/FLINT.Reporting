/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.selection;

import global.moja.databases.configurations.DatabaseConfig;
import global.moja.databases.daos.QueryParameters;
import global.moja.databases.models.Database;
import global.moja.databases.util.builders.DatabaseBuilder;
import global.moja.databases.util.builders.QueryWhereClauseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class SelectDatabasesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Databases records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Databases records if found
     */
    public Flux<Database> selectDatabases(QueryParameters parameters) {

        log.trace("Entering selectDatabases()");

        String query =
                "SELECT * FROM database" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new DatabaseBuilder()
                                                .id(rs.getLong("id"))
                                                .label(rs.getString("label"))
                                                .description(rs.getString("description"))
                                                .url(rs.getString("url"))
                                                .startYear(rs.getInt("start_year"))
                                                .endYear(rs.getInt("end_year"))
                                                .processed(rs.getBoolean("processed"))
                                                .published(rs.getBoolean("published"))
                                                .archived(rs.getBoolean("archived"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
