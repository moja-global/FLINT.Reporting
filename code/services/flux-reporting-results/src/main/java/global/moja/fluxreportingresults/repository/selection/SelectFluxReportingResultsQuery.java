/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.repository.selection;

import global.moja.fluxreportingresults.daos.QueryParameters;
import global.moja.fluxreportingresults.util.builders.FluxReportingResultBuilder;
import global.moja.fluxreportingresults.util.builders.QueryWhereClauseBuilder;
import global.moja.fluxreportingresults.configurations.DatabaseConfig;
import global.moja.fluxreportingresults.models.FluxReportingResult;
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
public class SelectFluxReportingResultsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Flux Reporting Results records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Flux Reporting Results records if found
     */
    public Flux<FluxReportingResult> selectFluxReportingResults(Long databaseId, QueryParameters parameters) {

        log.trace("Entering selectFluxReportingResults()");

        String query =
                "SELECT " +
                        "flux_reporting_results_id_pk, " +
                        "date_dimension_id_fk, " +
                        "location_dimension_id_fk, " +
                        "fluxtypeinfo_dimension_id_fk, " +
                        "source_poolinfo_dimension_id_fk, " +
                        "sink_poolinfo_dimension_id_fk, " +
                        "round( flux::numeric, 12 ) flux, " +
                        "itemcount " +
                        "FROM flux_reporting_results " +
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
                                                        new FluxReportingResultBuilder()
                                                                .id(rs.getLong("flux_reporting_results_id_pk"))
                                                                .dateId(rs.getLong("date_dimension_id_fk"))
                                                                .locationId(rs.getLong("location_dimension_id_fk"))
                                                                .fluxTypeId(rs.getLong("fluxtypeinfo_dimension_id_fk"))
                                                                .sourcePoolId(rs.getLong("source_poolinfo_dimension_id_fk"))
                                                                .sinkPoolId(rs.getLong("sink_poolinfo_dimension_id_fk"))
                                                                .flux(rs.getDouble("flux"))
                                                                .itemCount(rs.getLong("itemcount"))
                                                                .build())));


    }

}
