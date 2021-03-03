/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.updation;

import global.moja.databases.configurations.DatabaseConfig;
import global.moja.databases.models.Database;
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
public class UpdateDatabaseQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Database record
	 * @param database a bean containing the Database record details
	 * @return the number of Databases records affected by the query i.e updated
	 */
	public Mono<Integer> updateDatabase(Database database){
		
		log.trace("Entering updateDatabase()");

		String query =
				"UPDATE database SET " +
						"label = ?, " +
						"description  = ?, " +
						"url = ?, " +
						"start_year = ?, " +
						"end_year = ?, " +
						"processed = ?, " +
						"published = ?, " +
						"archived = ? " +
						"WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							database.getLabel(),
							database.getDescription(),
							database.getUrl(),
							database.getStartYear(),
							database.getEndYear(),
							database.getProcessed(),
							database.getPublished(),
							database.getArchived(),
							database.getId())
					.counts());
	}
}
