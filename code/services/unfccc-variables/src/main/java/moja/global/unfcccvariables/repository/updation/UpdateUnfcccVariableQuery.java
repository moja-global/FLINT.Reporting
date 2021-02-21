/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.repository.updation;

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
public class UpdateUnfcccVariableQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Updates a unfcccVariable record
	 * @param unfcccVariable a bean containing the record's details
	 * @return the number of records affected by the query i.e updated
	 */
	public Mono<Integer> updateUnfcccVariable(UnfcccVariable unfcccVariable){
		
		log.trace("Entering updateUnfcccVariable()");

		String query = "UPDATE unfccc_variable SET name = ?, measure = ?, abbreviation = ?, unit_id = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							unfcccVariable.getName(),
							unfcccVariable.getMeasure(),
							unfcccVariable.getAbbreviation(),
							unfcccVariable.getUnitId(),
							unfcccVariable.getId())
					.counts());
	}
}
