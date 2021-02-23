/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.repository.insertion;

import io.reactivex.Flowable;
import moja.global.fluxestounfcccvariables.configurations.DatabaseConfig;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class InsertFluxesToUnfcccVariablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts new fluxes to UNFCCC variable records into the database
	 * @param fluxToUnfcccVariables an array of beans containing the records' details
	 * @return the unique identifiers of the newly inserted fluxes to UNFCCC variable records
	 */	
	public Flux<Long> insertFluxesToUnfcccVariables(FluxToUnfcccVariable[] fluxToUnfcccVariables) {

		log.trace("Entering insertFluxesToUnfcccVariables");

		String query = "INSERT INTO flux_to_unfccc_variable(start_pool_id, end_pool_id, unfccc_variable_id, rule) VALUES(?,?,?,?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(fluxToUnfcccVariables))
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

	private Flowable getParametersListStream(FluxToUnfcccVariable[] fluxToUnfcccVariables) {
		
		List<List> parameters = new ArrayList<>();
		
		for (FluxToUnfcccVariable p : fluxToUnfcccVariables) {
			parameters.add(Arrays.asList(
					p.getStartPoolId(),
					p.getEndPoolId(),
					p.getUnfcccVariableId(),
					p.getRule()));
		}

		return Flowable.fromIterable(parameters);
	}

}
