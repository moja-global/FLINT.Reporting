/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.repository.selection;

import global.moja.pools.util.builders.PoolBuilder;
import global.moja.pools.configurations.DatabaseConfig;
import global.moja.pools.models.Pool;
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
public class SelectPoolQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Pool record from the database given its unique identifier
	 * @param id the unique identifier of the Pool record to be selected
	 * @return the Pool record with the given id if found
	 */
	public Mono<Pool> selectPool(Long id) {

		log.trace("Entering selectPool()");

		String query = "SELECT * FROM pool WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new PoolBuilder()
									.id(rs.getLong("id"))
									.name(rs.getString("name"))
									.description(rs.getString("description"))
									.version(rs.getInt("version"))
									.build()));
	}

}
