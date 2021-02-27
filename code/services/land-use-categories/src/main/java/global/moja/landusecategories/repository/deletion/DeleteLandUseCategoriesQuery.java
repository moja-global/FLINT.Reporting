/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.deletion;

import global.moja.landusecategories.configurations.DatabaseConfig;
import global.moja.landusecategories.daos.QueryParameters;
import global.moja.landusecategories.util.builders.QueryWhereClauseBuilder;
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
public class DeleteLandUseCategoriesQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Land Use Categories records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Land Use Categories records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteLandUseCategories(QueryParameters parameters){

		log.trace("Entering deleteLandUseCategories");

		String query =
				"DELETE FROM land_use_category" +
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
