/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.repository.updation;

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
public class UpdateFluxToUnfcccVariableQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Updates a flux to unfccc variable record
	 * @param fluxToUnfcccVariable a bean containing the record's details
	 * @return the number of records affected by the query i.e updated
	 */
	public Mono<Integer> updateFluxToUnfcccVariable(FluxToUnfcccVariable fluxToUnfcccVariable){
		
		log.trace("Entering updateFluxToUnfcccVariable()");

		String query = "UPDATE flux_to_unfccc_variable SET start_pool_id = ?, end_pool_id = ?, unfccc_variable_id = ?, rule = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							fluxToUnfcccVariable.getStartPoolId(),
							fluxToUnfcccVariable.getEndPoolId(),
							fluxToUnfcccVariable.getUnfcccVariableId(),
							fluxToUnfcccVariable.getRule(),
							fluxToUnfcccVariable.getId())
					.counts());
	}
}
