/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.repository.selection;

import global.moja.reportingframework.configurations.DatabaseConfig;
import global.moja.reportingframework.daos.QueryParameters;
import global.moja.reportingframework.util.builders.QueryWhereClauseBuilder;
import global.moja.reportingframework.util.builders.ReportingFrameworkBuilder;
import global.moja.reportingframework.models.ReportingFramework;
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
public class SelectReportingFrameworksQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Reporting Frameworks records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Reporting Frameworks records if found
     */
    public Flux<ReportingFramework> selectReportingFrameworks(QueryParameters parameters) {

        log.trace("Entering selectReportingFrameworks()");

        String query =
                "SELECT * FROM reporting_framework" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new ReportingFrameworkBuilder()
                                                .id(rs.getLong("id"))
                                                .name(rs.getString("name"))
                                                .description(rs.getString("description"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
