/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository.selection;

import global.moja.landusesfluxtypestoreportingtables.daos.QueryParameters;
import global.moja.landusesfluxtypestoreportingtables.util.builders.LandUseFluxTypeToReportingTableBuilder;
import global.moja.landusesfluxtypestoreportingtables.util.builders.QueryWhereClauseBuilder;
import global.moja.landusesfluxtypestoreportingtables.configurations.DatabaseConfig;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
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
public class SelectLandUsesFluxTypesToReportingTablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Land Uses Flux Types To Reporting Tables records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Land Uses Flux Types To Reporting Tables records if found
     */
    public Flux<LandUseFluxTypeToReportingTable> selectLandUsesFluxTypesToReportingTables(QueryParameters parameters) {

        log.trace("Entering selectLandUsesFluxTypesToReportingTables()");

        String query =
                "SELECT * FROM land_use_flux_type_to_reporting_table" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new LandUseFluxTypeToReportingTableBuilder()
                                                .id(rs.getLong("id"))
                                                .landUseFluxTypeId(rs.getLong("land_use_flux_type_id"))
                                                .emissionTypeId(rs.getLong("emission_type_id"))
                                                .reportingTableId(rs.getLong("reporting_table_id"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
