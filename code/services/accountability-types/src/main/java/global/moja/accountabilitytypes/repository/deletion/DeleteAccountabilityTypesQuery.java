/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository.deletion;

import global.moja.accountabilitytypes.configurations.DatabaseConfig;
import global.moja.accountabilitytypes.daos.QueryParameters;
import global.moja.accountabilitytypes.util.builders.QueryWhereClauseBuilder;
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
public class DeleteAccountabilityTypesQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Accountability Types records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Accountability Types records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteAccountabilityTypes(QueryParameters parameters){

		log.trace("Entering deleteAccountabilityTypes");

		String query =
				"DELETE FROM accountability_type" +
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
