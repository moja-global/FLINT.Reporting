/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.repository.selection;

import global.moja.dates.configurations.DatabaseConfig;
import global.moja.dates.daos.QueryParameters;
import global.moja.dates.models.Date;
import global.moja.dates.util.builders.DateBuilder;
import global.moja.dates.util.builders.QueryWhereClauseBuilder;
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
public class SelectDatesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Dates records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Dates records if found
     */
    public Flux<Date> selectDates(Long databaseId, QueryParameters parameters) {

        log.trace("Entering selectDates()");

        String query =
                "SELECT * FROM date_dimension" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux
                        .from(databaseConfig.getDatabase(databaseId))
                        .flatMap(database ->
                                Flux.from(
                                        database
                                                .select(query)
                                                .get(rs ->
                                                        new DateBuilder()
                                                                .id(rs.getLong("date_dimension_id_pk"))
                                                                .year(rs.getInt("year"))
                                                                .build())));

    }

}
