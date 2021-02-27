/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.repository.selection;

import global.moja.fluxestoreportingvariables.configurations.DatabaseConfig;
import global.moja.fluxestoreportingvariables.daos.QueryParameters;
import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
import global.moja.fluxestoreportingvariables.util.builders.FluxesToReportingVariableBuilder;
import global.moja.fluxestoreportingvariables.util.builders.QueryWhereClauseBuilder;
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
public class SelectFluxesToReportingVariablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific fluxes to reporting variable records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of fluxes to reporting variable records if found
     */
    public Flux<FluxToReportingVariable> selectFluxesToReportingVariables(QueryParameters parameters) {

        log.trace("Entering selectFluxesToReportingVariables()");

        String query =
                "SELECT * FROM flux_to_reporting_variable" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new FluxesToReportingVariableBuilder()
                                                .id(rs.getLong("id"))
                                                .startPoolId(rs.getLong("start_pool_id"))
                                                .endPoolId(rs.getLong("end_pool_id"))
                                                .reportingVariableId(rs.getLong("reporting_variable_id"))
                                                .rule(rs.getString("rule"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
