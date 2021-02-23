/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.repository;

import moja.global.fluxestounfcccvariables.daos.QueryParameters;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.repository.deletion.DeleteFluxToUnfcccVariableQuery;
import moja.global.fluxestounfcccvariables.repository.deletion.DeleteFluxesToUnfcccVariablesQuery;
import moja.global.fluxestounfcccvariables.repository.selection.SelectFluxesToUnfcccVariablesQuery;
import moja.global.fluxestounfcccvariables.repository.updation.UpdateFluxToUnfcccVariableQuery;
import moja.global.fluxestounfcccvariables.repository.insertion.InsertFluxToUnfcccVariableQuery;
import moja.global.fluxestounfcccvariables.repository.insertion.InsertFluxesToUnfcccVariablesQuery;
import moja.global.fluxestounfcccvariables.repository.selection.SelectFluxToUnfcccVariableQuery;
import moja.global.fluxestounfcccvariables.repository.updation.UpdateFluxesToUnfcccVariablesQuery;
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
public class FluxesToUnfcccVariablesRepository {

	
	@Autowired
	InsertFluxToUnfcccVariableQuery insertFluxToUnfcccVariableQuery;
	
	@Autowired
	InsertFluxesToUnfcccVariablesQuery insertFluxesToUnfcccVariablesQuery;
	
	@Autowired
	SelectFluxToUnfcccVariableQuery selectFluxToUnfcccVariableQuery;
	
	@Autowired
	SelectFluxesToUnfcccVariablesQuery selectFluxesToUnfcccVariablesQuery;

	@Autowired
	UpdateFluxToUnfcccVariableQuery updateFluxToUnfcccVariableQuery;
	
	@Autowired
	UpdateFluxesToUnfcccVariablesQuery updateFluxesToUnfcccVariablesQuery;
	
	@Autowired
	DeleteFluxToUnfcccVariableQuery deleteFluxToUnfcccVariableQuery;
	
	@Autowired
	DeleteFluxesToUnfcccVariablesQuery deleteFluxesToUnfcccVariablesQuery;


	public Mono<Long> insertFluxToUnfcccVariable(FluxToUnfcccVariable fluxToUnfcccVariable) {
		return insertFluxToUnfcccVariableQuery.insertFluxToUnfcccVariable(fluxToUnfcccVariable);
	}
	
	public Flux<Long> insertFluxesToUnfcccVariables(FluxToUnfcccVariable[] fluxToUnfcccVariables) {
		return insertFluxesToUnfcccVariablesQuery.insertFluxesToUnfcccVariables(fluxToUnfcccVariables);
	}

	public Mono<FluxToUnfcccVariable> selectFluxToUnfcccVariable(Long id) {
		return selectFluxToUnfcccVariableQuery.selectFluxToUnfcccVariable(id);
	}
	
	public Flux<FluxToUnfcccVariable> selectFluxesToUnfcccVariables(QueryParameters parameters) {
		return selectFluxesToUnfcccVariablesQuery.selectFluxesToUnfcccVariables(parameters);
	}

	public Mono<Integer> updateFluxToUnfcccVariable(FluxToUnfcccVariable fluxToUnfcccVariable) {
		return updateFluxToUnfcccVariableQuery.updateFluxToUnfcccVariable(fluxToUnfcccVariable);
	}
	
	public Flux<Integer> updateFluxesToUnfcccVariables(FluxToUnfcccVariable[] fluxToUnfcccVariables) {
		return updateFluxesToUnfcccVariablesQuery.updateFluxesToUnfcccVariables(fluxToUnfcccVariables);
	}	
	
	public Mono<Integer> deleteFluxToUnfcccVariableById(Long id) {
		return deleteFluxToUnfcccVariableQuery.deleteFluxToUnfcccVariable(id);
	}
	
	public Mono<Integer> deleteFluxesToUnfcccVariables(QueryParameters parameters) {
		return deleteFluxesToUnfcccVariablesQuery.deleteFluxesToUnfcccVariables(parameters);
	}


}
