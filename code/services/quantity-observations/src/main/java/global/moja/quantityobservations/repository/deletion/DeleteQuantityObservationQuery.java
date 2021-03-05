/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.deletion;

import global.moja.quantityobservations.configurations.DatabaseConfig;
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
public class DeleteQuantityObservationQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes a Quantity Observation record from the database
	 * @param id the unique identifier of the Quantity Observation record to be deleted
	 * @return the number of Quantity Observations records affected by the query i.e deleted
	 */	
	public Mono<Integer> deleteQuantityObservation(Long id){

		log.trace("Entering deleteQuantityObservation");

		String query = "DELETE FROM quantity_observation WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(id)
					.counts());
	}

}
