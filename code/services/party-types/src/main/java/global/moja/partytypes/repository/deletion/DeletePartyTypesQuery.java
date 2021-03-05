/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.repository.deletion;

import global.moja.partytypes.configurations.DatabaseConfig;
import global.moja.partytypes.daos.QueryParameters;
import global.moja.partytypes.util.builders.QueryWhereClauseBuilder;
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
public class DeletePartyTypesQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Party Types records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Party Types records affected by the query i.e deleted
	 */		
	public Mono<Integer> deletePartyTypes(QueryParameters parameters){

		log.trace("Entering deletePartyTypes");

		String query =
				"DELETE FROM party_type" +
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
