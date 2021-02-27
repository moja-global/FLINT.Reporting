/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.repository.insertion;

import io.reactivex.Flowable;
import global.moja.fluxestoreportingvariables.configurations.DatabaseConfig;
import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
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
public class InsertFluxesToReportingVariablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts new fluxes to reporting variable records into the database
	 * @param fluxToReportingVariables an array of beans containing the records' details
	 * @return the unique identifiers of the newly inserted fluxes to reporting variable records
	 */	
	public Flux<Long> insertFluxesToReportingVariables(FluxToReportingVariable[] fluxToReportingVariables) {

		log.trace("Entering insertFluxesToReportingVariables");

		String query = "INSERT INTO flux_to_reporting_variable(start_pool_id, end_pool_id, reporting_variable_id, rule) VALUES(?,?,?,?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(fluxToReportingVariables))
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

	private Flowable getParametersListStream(FluxToReportingVariable[] fluxToReportingVariables) {
		
		List<List> parameters = new ArrayList<>();
		
		for (FluxToReportingVariable p : fluxToReportingVariables) {
			parameters.add(Arrays.asList(
					p.getStartPoolId(),
					p.getEndPoolId(),
					p.getReportingVariableId(),
					p.getRule()));
		}

		return Flowable.fromIterable(parameters);
	}

}
