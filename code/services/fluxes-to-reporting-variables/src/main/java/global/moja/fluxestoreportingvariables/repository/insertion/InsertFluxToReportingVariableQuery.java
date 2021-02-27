/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.repository.insertion;

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
public class InsertFluxToReportingVariableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new flux to unfccc variable record into the database
	 * @param fluxToReportingVariable a bean containing the record's details
	 * @return the unique identifier of the newly inserted flux to unfccc variable record
	 */
	public Mono<Long> insertFluxToReportingVariable(FluxToReportingVariable fluxToReportingVariable){
		
		log.trace("Entering insertFluxToReportingVariable()");

		String query = "INSERT INTO flux_to_reporting_variable(start_pool_id, end_pool_id, reporting_variable_id, rule) VALUES(?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							fluxToReportingVariable.getStartPoolId(),
							fluxToReportingVariable.getEndPoolId(),
							fluxToReportingVariable.getReportingVariableId(),
							fluxToReportingVariable.getRule())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}
