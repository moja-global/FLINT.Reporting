/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.repository.updation;

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
public class UpdatePoolQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Pool record
	 * @param pool a bean containing the Pool record details
	 * @return the number of Pools records affected by the query i.e updated
	 */
	public Mono<Integer> updatePool(Pool pool){
		
		log.trace("Entering updatePool()");

		String query = "UPDATE pool SET name = ?, description = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							pool.getName(),
							pool.getDescription(),
							pool.getId())
					.counts());
	}
}
