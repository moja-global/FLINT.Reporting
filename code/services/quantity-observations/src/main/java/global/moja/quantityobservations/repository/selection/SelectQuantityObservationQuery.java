/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.selection;

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
public class SelectQuantityObservationQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Quantity Observation record from the database given its unique identifier
	 * @param id the unique identifier of the Quantity Observation record to be selected
	 * @return the Quantity Observation record with the given id if found
	 */
	public Mono<QuantityObservation> selectQuantityObservation(Long id) {

		log.trace("Entering selectQuantityObservation()");

		String query = "SELECT * FROM quantity_observation WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							QuantityObservation.builder()
									.id(rs.getLong("id"))
									.observationTypeId(rs.getLong("observation_type_id"))
									.taskId(rs.getLong("task_id"))
									.partyId(rs.getLong("party_id"))
									.databaseId(rs.getLong("database_id"))
									.landUseCategoryId(rs.getLong("land_use_category_id"))
									.reportingTableId(rs.getLong("reporting_table_id"))
									.reportingVariableId(rs.getLong("reporting_variable_id"))
									.year(rs.getInt("year"))
									.amount(rs.getDouble("amount"))
									.unitId(rs.getLong("unit_id"))
									.version(rs.getInt("version"))
									.build()));
	}

}
