/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.repository.insertion;

import moja.global.unfcccvariables.configurations.DatabaseConfig;
import moja.global.unfcccvariables.models.UnfcccVariable;
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
public class InsertUnfcccVariableQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts a new unfcccVariable record into the database
	 * @param unfcccVariable a bean containing the record's details
	 * @return the unique identifier of the newly inserted unfcccVariable record
	 */
	public Mono<Long> insertUnfcccVariable(UnfcccVariable unfcccVariable){
		
		log.trace("Entering insertUnfcccVariable()");

		String query = "INSERT INTO unfccc_variable(name, measure, abbreviation, unit_id) VALUES(?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							unfcccVariable.getName(),
							unfcccVariable.getMeasure(),
							unfcccVariable.getAbbreviation(),
							unfcccVariable.getUnitId())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}
