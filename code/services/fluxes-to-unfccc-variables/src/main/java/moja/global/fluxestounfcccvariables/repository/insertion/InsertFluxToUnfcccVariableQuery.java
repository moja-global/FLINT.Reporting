/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.repository.insertion;

import moja.global.fluxestounfcccvariables.configurations.DatabaseConfig;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
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
public class InsertFluxToUnfcccVariableQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts a new flux to unfccc variable record into the database
	 * @param fluxToUnfcccVariable a bean containing the record's details
	 * @return the unique identifier of the newly inserted flux to unfccc variable record
	 */
	public Mono<Long> insertFluxToUnfcccVariable(FluxToUnfcccVariable fluxToUnfcccVariable){
		
		log.trace("Entering insertFluxToUnfcccVariable()");

		String query = "INSERT INTO flux_to_unfccc_variable(start_pool_id, end_pool_id, unfccc_variable_id, rule) VALUES(?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							fluxToUnfcccVariable.getStartPoolId(),
							fluxToUnfcccVariable.getEndPoolId(),
							fluxToUnfcccVariable.getUnfcccVariableId(),
							fluxToUnfcccVariable.getRule())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}
