/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository.deletion;

import global.moja.quantityobservations.configurations.DatabaseConfig;
import global.moja.quantityobservations.daos.QueryParameters;
import global.moja.quantityobservations.util.builders.QueryWhereClauseBuilder;
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
public class DeleteQuantityObservationsQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Quantity Observations records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Quantity Observations records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteQuantityObservations(QueryParameters parameters){

		log.trace("Entering deleteQuantityObservations");

		String query =
				"DELETE FROM quantity_observation" +
						new QueryWhereClauseBuilder()
								.queryParameters(parameters)
								.build();

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.counts());
	}	

}
