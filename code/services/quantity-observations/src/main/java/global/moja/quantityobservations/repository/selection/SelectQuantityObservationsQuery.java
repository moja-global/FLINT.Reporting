/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.selection;

import global.moja.quantityobservations.configurations.DatabaseConfig;
import global.moja.quantityobservations.daos.QueryParameters;
import global.moja.quantityobservations.util.builders.QuantityObservationBuilder;
import global.moja.quantityobservations.models.QuantityObservation;
import global.moja.quantityobservations.util.builders.QueryWhereClauseBuilder;
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
public class SelectQuantityObservationsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Quantity Observations records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Quantity Observations records if found
     */
    public Flux<QuantityObservation> selectQuantityObservations(QueryParameters parameters) {

        log.trace("Entering selectQuantityObservations()");

        String query =
                "SELECT * FROM quantity_observation" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new QuantityObservationBuilder()
                                                .id(rs.getLong("id"))
                                                .taskId(rs.getLong("task_id"))
                                                .partyId(rs.getLong("party_id"))
                                                .reportingVariableId(rs.getLong("reporting_variable_id"))
                                                .year(rs.getInt("year"))
                                                .amount(rs.getDouble("amount"))
                                                .unitId(rs.getLong("unit_id"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}
