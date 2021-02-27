/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository.insertion;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.configurations.DatabaseConfig;
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
public class InsertCoverTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Cover Type record into the database
	 * @param coverType a bean containing the Cover Type record details
	 * @return the unique identifier of the newly inserted Cover Type record
	 */
	public Mono<Long> insertCoverType(CoverType coverType){
		
		log.trace("Entering insertCoverType()");

		String query = "INSERT INTO cover_type(code,description) VALUES(?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							coverType.getCode(),
							coverType.getDescription())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}
