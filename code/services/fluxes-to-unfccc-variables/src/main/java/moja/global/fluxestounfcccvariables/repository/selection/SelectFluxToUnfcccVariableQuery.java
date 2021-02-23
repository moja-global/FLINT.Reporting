/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.repository.selection;

import moja.global.fluxestounfcccvariables.configurations.DatabaseConfig;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.util.builders.FluxesToUnfcccVariableBuilder;
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
public class SelectFluxToUnfcccVariableQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects a flux to UNFCCC variable record from the database given its unique identifier
	 * @param id the unique identifier of the record to be selected
	 * @return the record with the given id if found
	 */
	public Mono<FluxToUnfcccVariable> selectFluxToUnfcccVariable(Long id) {

		log.trace("Entering selectFluxToUnfcccVariable()");

		String query = "SELECT * FROM flux_to_unfccc_variable WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new FluxesToUnfcccVariableBuilder()
									.id(rs.getLong("id"))
									.startPoolId(rs.getLong("start_pool_id"))
									.endPoolId(rs.getLong("end_pool_id"))
									.unfcccVariableId(rs.getLong("unfccc_variable_id"))
									.rule(rs.getString("rule"))
									.version(rs.getInt("version"))
									.build()));
	}

}
