/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.selection;

import global.moja.databases.configurations.DatabaseConfig;
import global.moja.databases.models.Database;
import global.moja.databases.util.builders.DatabaseBuilder;
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
public class SelectDatabaseQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Database record from the database given its unique identifier
	 * @param id the unique identifier of the Database record to be selected
	 * @return the Database record with the given id if found
	 */
	public Mono<Database> selectDatabase(Long id) {

		log.trace("Entering selectDatabase()");

		String query = "SELECT * FROM database WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new DatabaseBuilder()
									.id(rs.getLong("id"))
									.label(rs.getString("label"))
									.description(rs.getString("description"))
									.url(rs.getString("url"))
									.startYear(rs.getInt("start_year"))
									.endYear(rs.getInt("end_year"))
									.processed(rs.getBoolean("processed"))
									.published(rs.getBoolean("published"))
									.archived(rs.getBoolean("archived"))
									.version(rs.getInt("version"))
									.build()));
	}

}
