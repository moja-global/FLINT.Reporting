/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.insertion;

import global.moja.quantityobservations.configurations.DatabaseConfig;
import global.moja.quantityobservations.models.QuantityObservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InsertQuantityObservationQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts a new Quantity Observation record into the database
     *
     * @param quantityObservation a bean containing the Quantity Observation record details
     * @return the unique identifier of the newly inserted Quantity Observation record
     */
    public Mono<Long> insertQuantityObservation(QuantityObservation quantityObservation) {

        log.trace("Entering insertQuantityObservation()");

        String query =
                "INSERT INTO quantity_observation(task_id,party_id,reporting_variable_id,year,amount,unit_id) " +
                        "VALUES(?,?,?,?,?,?)";

        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(
                                        quantityObservation.getTaskId(),
                                        quantityObservation.getPartyId(),
                                        quantityObservation.getReportingVariableId(),
                                        quantityObservation.getYear(),
                                        quantityObservation.getAmount(),
                                        quantityObservation.getUnitId())
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

}
