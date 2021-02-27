/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.repository;

import global.moja.fluxestoreportingvariables.daos.QueryParameters;
import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
import global.moja.fluxestoreportingvariables.repository.insertion.InsertFluxToReportingVariableQuery;
import global.moja.fluxestoreportingvariables.repository.insertion.InsertFluxesToReportingVariablesQuery;
import global.moja.fluxestoreportingvariables.repository.selection.SelectFluxToReportingVariableQuery;
import global.moja.fluxestoreportingvariables.repository.updation.UpdateFluxToReportingVariableQuery;
import global.moja.fluxestoreportingvariables.repository.updation.UpdateFluxesToReportingVariablesQuery;
import global.moja.fluxestoreportingvariables.repository.deletion.DeleteFluxToReportingVariableQuery;
import global.moja.fluxestoreportingvariables.repository.deletion.DeleteFluxesToReportingVariablesQuery;
import global.moja.fluxestoreportingvariables.repository.selection.SelectFluxesToReportingVariablesQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class FluxesToReportingVariablesRepository {

	
	@Autowired
	InsertFluxToReportingVariableQuery insertFluxToReportingVariableQuery;
	
	@Autowired
	InsertFluxesToReportingVariablesQuery insertFluxesToReportingVariablesQuery;
	
	@Autowired
	SelectFluxToReportingVariableQuery selectFluxToReportingVariableQuery;
	
	@Autowired
	SelectFluxesToReportingVariablesQuery selectFluxesToReportingVariablesQuery;

	@Autowired
	UpdateFluxToReportingVariableQuery updateFluxToReportingVariableQuery;
	
	@Autowired
	UpdateFluxesToReportingVariablesQuery updateFluxesToReportingVariablesQuery;
	
	@Autowired
	DeleteFluxToReportingVariableQuery deleteFluxToReportingVariableQuery;
	
	@Autowired
	DeleteFluxesToReportingVariablesQuery deleteFluxesToReportingVariablesQuery;


	public Mono<Long> insertFluxToReportingVariable(FluxToReportingVariable fluxToReportingVariable) {
		return insertFluxToReportingVariableQuery.insertFluxToReportingVariable(fluxToReportingVariable);
	}
	
	public Flux<Long> insertFluxesToReportingVariables(FluxToReportingVariable[] fluxToReportingVariables) {
		return insertFluxesToReportingVariablesQuery.insertFluxesToReportingVariables(fluxToReportingVariables);
	}

	public Mono<FluxToReportingVariable> selectFluxToReportingVariable(Long id) {
		return selectFluxToReportingVariableQuery.selectFluxToReportingVariable(id);
	}
	
	public Flux<FluxToReportingVariable> selectFluxesToReportingVariables(QueryParameters parameters) {
		return selectFluxesToReportingVariablesQuery.selectFluxesToReportingVariables(parameters);
	}

	public Mono<Integer> updateFluxToReportingVariable(FluxToReportingVariable fluxToReportingVariable) {
		return updateFluxToReportingVariableQuery.updateFluxToReportingVariable(fluxToReportingVariable);
	}
	
	public Flux<Integer> updateFluxesToReportingVariables(FluxToReportingVariable[] fluxToReportingVariables) {
		return updateFluxesToReportingVariablesQuery.updateFluxesToReportingVariables(fluxToReportingVariables);
	}	
	
	public Mono<Integer> deleteFluxToReportingVariableById(Long id) {
		return deleteFluxToReportingVariableQuery.deleteFluxToReportingVariable(id);
	}
	
	public Mono<Integer> deleteFluxesToReportingVariables(QueryParameters parameters) {
		return deleteFluxesToReportingVariablesQuery.deleteFluxesToReportingVariables(parameters);
	}


}
