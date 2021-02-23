/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.repository.selection;

import lombok.extern.slf4j.Slf4j;
import moja.global.fluxestounfcccvariables.configurations.DatabaseConfig;
import moja.global.fluxestounfcccvariables.daos.QueryParameters;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.util.builders.FluxesToUnfcccVariableBuilder;
import moja.global.fluxestounfcccvariables.util.builders.QueryWhereClauseBuilder;
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
public class SelectFluxesToUnfcccVariablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific fluxes to UNFCCC variable records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of fluxes to UNFCCC variable records if found
     */
    public Flux<FluxToUnfcccVariable> selectFluxesToUnfcccVariables(QueryParameters parameters) {

        log.trace("Entering selectFluxesToUnfcccVariables()");

        String query =
                "SELECT * FROM flux_to_unfccc_variable" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new FluxesToUnfcccVariableBuilder()
                                                .id(rs.getLong("id"))
                                                .startPoolId(rs.getLong("start_pool_id"))
                                                .endPoolId(rs.getLong("end_pool_id"))
                                                .unfcccVariableId(rs.getLong("unfccc_variable_id"))
                                                .rule(rs.getString("rule"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
