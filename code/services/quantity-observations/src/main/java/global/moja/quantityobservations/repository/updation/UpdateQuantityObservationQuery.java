/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.updation;

import global.moja.quantityobservations.configurations.DatabaseConfig;
import global.moja.quantityobservations.models.QuantityObservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateQuantityObservationQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Quantity Observation record
	 * @param quantityObservation a bean containing the Quantity Observation record details
	 * @return the number of Quantity Observations records affected by the query i.e updated
	 */
	public Mono<Integer> updateQuantityObservation(QuantityObservation quantityObservation){
		
		log.trace("Entering updateQuantityObservation()");

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
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							quantityObservation.getTaskId(),
							quantityObservation.getPartyId(),
							quantityObservation.getDatabaseId(),
							quantityObservation.getReportingTableId(),
							quantityObservation.getReportingVariableId(),
							quantityObservation.getYear(),
							quantityObservation.getAmount(),
							quantityObservation.getUnitId(),
							quantityObservation.getId())
					.counts());
	}
}
