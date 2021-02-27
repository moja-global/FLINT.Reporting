/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.repository.selection;

import global.moja.reportingtable.configurations.DatabaseConfig;
import global.moja.reportingtable.daos.QueryParameters;
import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.util.builders.QueryWhereClauseBuilder;
import global.moja.reportingtable.util.builders.ReportingTableBuilder;
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
public class SelectReportingTablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Reporting Tables records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Reporting Tables records if found
     */
    public Flux<ReportingTable> selectReportingTables(QueryParameters parameters) {

        log.trace("Entering selectReportingTables()");

        String query =
                "SELECT * FROM reporting_table" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new ReportingTableBuilder()
                                                .id(rs.getLong("id"))
                                                .reportingFrameworkId(rs.getLong("reporting_framework_id"))
                                                .number(rs.getString("number"))
                                                .name(rs.getString("name"))
                                                .description(rs.getString("description"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
