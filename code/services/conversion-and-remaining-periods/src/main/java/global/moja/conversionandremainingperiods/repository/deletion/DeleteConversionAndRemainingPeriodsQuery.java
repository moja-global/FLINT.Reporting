/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository.deletion;

import global.moja.conversionandremainingperiods.configurations.DatabaseConfig;
import global.moja.conversionandremainingperiods.daos.QueryParameters;
import global.moja.conversionandremainingperiods.util.builders.QueryWhereClauseBuilder;
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
public class DeleteConversionAndRemainingPeriodsQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Conversion and Remaining Periods records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Conversion and Remaining Periods records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteConversionAndRemainingPeriods(QueryParameters parameters){

		log.trace("Entering deleteConversionAndRemainingPeriods");

		String query =
				"DELETE FROM conversion_and_remaining_period" +
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
