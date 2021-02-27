/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository.deletion;

import global.moja.covertypes.configurations.DatabaseConfig;
import global.moja.covertypes.daos.QueryParameters;
import global.moja.covertypes.util.builders.QueryWhereClauseBuilder;
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
public class DeleteCoverTypesQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Cover Types records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Cover Types records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteCoverTypes(QueryParameters parameters){

		log.trace("Entering deleteCoverTypes");

		String query =
				"DELETE FROM cover_type" +
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
