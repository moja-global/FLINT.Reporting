/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.repository.deletion;

import global.moja.fluxestoreportingvariables.configurations.DatabaseConfig;
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
public class DeleteFluxToReportingVariableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes a flux to reporting variable record from the database
	 * @param id the unique identifier of the flux to reporting variable record to be deleted
	 * @return the number of records affected by the query i.e deleted
	 */	
	public Mono<Integer> deleteFluxToReportingVariable(Long id){

		log.trace("Entering deleteFluxToReportingVariable");

		String query = "DELETE FROM flux_to_reporting_variable WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(id)
					.counts());
	}

}
