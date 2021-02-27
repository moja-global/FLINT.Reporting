/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.repository.insertion;

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
public class InsertPoolQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Pool record into the database
	 * @param pool a bean containing the Pool record details
	 * @return the unique identifier of the newly inserted Pool record
	 */
	public Mono<Long> insertPool(Pool pool){
		
		log.trace("Entering insertPool()");

		String query = "INSERT INTO pool(name,description) VALUES(?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							pool.getName(),
							pool.getDescription())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}
