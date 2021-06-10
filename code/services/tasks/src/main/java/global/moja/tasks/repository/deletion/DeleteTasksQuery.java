/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.repository.deletion;

import global.moja.tasks.configurations.DatabaseConfig;
import global.moja.tasks.daos.QueryParameters;
import global.moja.tasks.util.builders.QueryWhereClauseBuilder;
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
public class DeleteTasksQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes all or specific Tasks records from the database depending on whether or not
	 * query parameters were supplied as part of the query
	 * @return the number of Tasks records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteTasks(QueryParameters parameters){

		log.trace("Entering deleteTasks");

		String query =
				"DELETE FROM task" +
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
