/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.updation;

import io.reactivex.Flowable;
import global.moja.quantityobservations.configurations.DatabaseConfig;
import global.moja.quantityobservations.models.QuantityObservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class UpdateQuantityObservationsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Quantity Observations records
     *
     * @param quantityObservations an array of beans containing the Quantity Observations records details
     * @return the number of Quantity Observations records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateQuantityObservations(QuantityObservation[] quantityObservations) {

        log.trace("Entering updateQuantityObservations()");

        String query = "UPDATE quantity_observation SET " +
                "task_id = ?, " +
                "party_id = ?, " +
                "database_id = ?, " +
                "reporting_table_id = ?, " +
                "reporting_variable_id = ?, " +
                "year = ?, " +
                "amount = ?, " +
                "unit_id = ? " +
                "WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(quantityObservations))
                                .counts());
    }

    private Flowable getParametersListStream(QuantityObservation[] quantityObservations) {

        List<List> temp = new ArrayList<>();

        for (QuantityObservation quantityObservation : quantityObservations) {
            temp.add(Arrays.asList(
                    quantityObservation.getTaskId(),
                    quantityObservation.getPartyId(),
                    quantityObservation.getDatabaseId(),
                    quantityObservation.getReportingTableId(),
                    quantityObservation.getReportingVariableId(),
                    quantityObservation.getYear(),
                    quantityObservation.getAmount(),
                    quantityObservation.getUnitId(),
                    quantityObservation.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}
