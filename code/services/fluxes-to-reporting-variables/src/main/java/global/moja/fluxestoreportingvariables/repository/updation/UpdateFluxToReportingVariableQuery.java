/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.repository.updation;

import global.moja.fluxestoreportingvariables.configurations.DatabaseConfig;
import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
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
public class UpdateFluxToReportingVariableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a flux to unfccc variable record
	 * @param fluxToReportingVariable a bean containing the record's details
	 * @return the number of records affected by the query i.e updated
	 */
	public Mono<Integer> updateFluxToReportingVariable(FluxToReportingVariable fluxToReportingVariable){
		
		log.trace("Entering updateFluxToReportingVariable()");

		String query = "UPDATE flux_to_reporting_variable SET start_pool_id = ?, end_pool_id = ?, reporting_variable_id = ?, rule = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							fluxToReportingVariable.getStartPoolId(),
							fluxToReportingVariable.getEndPoolId(),
							fluxToReportingVariable.getReportingVariableId(),
							fluxToReportingVariable.getRule(),
							fluxToReportingVariable.getId())
					.counts());
	}
}
