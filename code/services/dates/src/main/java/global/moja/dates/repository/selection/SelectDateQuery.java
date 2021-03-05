/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.repository.selection;

import global.moja.dates.configurations.DatabaseConfig;
import global.moja.dates.models.Date;
import global.moja.dates.util.builders.DateBuilder;
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
public class SelectDateQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Date record from the database given its unique identifier
	 * @param id the unique identifier of the Date record to be selected
	 * @return the Date record with the given id if found
	 */
	public Mono<Date> selectDate(Long databaseId, Long id) {

		log.trace("Entering selectDate()");

		String query = "SELECT * FROM date_dimension WHERE date_dimension_id_pk = ?";

		return
				databaseConfig
						.getDatabase(databaseId)
						.flatMap(database ->
								Mono.from(
										database
												.select(query)
												.parameters(id)
												.get(rs ->
														new DateBuilder()
																.id(rs.getLong("date_dimension_id_pk"))
																.year(rs.getInt("year"))
																.build())));

	}

}
