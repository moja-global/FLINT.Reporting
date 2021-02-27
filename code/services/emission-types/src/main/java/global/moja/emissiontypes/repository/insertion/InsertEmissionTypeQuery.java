/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.repository.insertion;

import global.moja.emissiontypes.configurations.DatabaseConfig;
import global.moja.emissiontypes.models.EmissionType;
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
public class InsertEmissionTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Emission Type record into the database
	 * @param emissionType a bean containing the Emission Type record details
	 * @return the unique identifier of the newly inserted Emission Type record
	 */
	public Mono<Long> insertEmissionType(EmissionType emissionType){
		
		log.trace("Entering insertEmissionType()");

		String query = "INSERT INTO emission_type(name,abbreviation,description) VALUES(?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							emissionType.getName(),
							emissionType.getAbbreviation(),
							emissionType.getDescription()
					)
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}
