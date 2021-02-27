/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.repository.selection;

import global.moja.fluxtypes.configurations.DatabaseConfig;
import global.moja.fluxtypes.daos.QueryParameters;
import global.moja.fluxtypes.models.FluxType;
import global.moja.fluxtypes.util.builders.FluxTypeBuilder;
import global.moja.fluxtypes.util.builders.QueryWhereClauseBuilder;
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
public class SelectFluxTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Flux Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Flux Types records if found
     */
    public Flux<FluxType> selectFluxTypes(QueryParameters parameters) {

        log.trace("Entering selectFluxTypes()");

        String query =
                "SELECT * FROM flux_type" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new FluxTypeBuilder()
                                                .id(rs.getLong("id"))
                                                .name(rs.getString("name"))
                                                .description(rs.getString("description"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
