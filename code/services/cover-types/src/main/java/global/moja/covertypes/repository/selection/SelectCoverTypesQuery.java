/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository.selection;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.configurations.DatabaseConfig;
import global.moja.covertypes.daos.QueryParameters;
import global.moja.covertypes.util.builders.CoverTypeBuilder;
import global.moja.covertypes.util.builders.QueryWhereClauseBuilder;
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
public class SelectCoverTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Cover Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Cover Types records if found
     */
    public Flux<CoverType> selectCoverTypes(QueryParameters parameters) {

        log.trace("Entering selectCoverTypes()");

        String query =
                "SELECT * FROM cover_type" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new CoverTypeBuilder()
                                                .id(rs.getLong("id"))
                                                .code(rs.getString("code"))
                                                .description(rs.getString("description"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
