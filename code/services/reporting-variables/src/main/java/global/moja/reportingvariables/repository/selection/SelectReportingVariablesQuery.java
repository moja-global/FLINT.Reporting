/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.repository.selection;

import global.moja.reportingvariables.configurations.DatabaseConfig;
import global.moja.reportingvariables.daos.QueryParameters;
import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.util.builders.ReportingVariableBuilder;
import global.moja.reportingvariables.util.builders.QueryWhereClauseBuilder;
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
public class SelectReportingVariablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Reporting Variables records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Reporting Variables records if found
     */
    public Flux<ReportingVariable> selectReportingVariables(QueryParameters parameters) {

        log.trace("Entering selectReportingVariables()");

        String query =
                "SELECT * FROM reporting_variable" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build() +
                        " ORDER BY id ASC";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new ReportingVariableBuilder()
                                                .id(rs.getLong("id"))
                                                .reportingFrameworkId(rs.getLong("reporting_framework_id"))
                                                .name(rs.getString("name"))
                                                .description(rs.getString("description"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
