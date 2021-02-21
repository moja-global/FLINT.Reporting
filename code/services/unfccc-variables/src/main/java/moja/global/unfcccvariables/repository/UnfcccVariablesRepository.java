/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.repository;

import moja.global.unfcccvariables.models.UnfcccVariable;
import moja.global.unfcccvariables.repository.deletion.DeleteAllUnfcccVariablesQuery;
import moja.global.unfcccvariables.repository.deletion.DeleteUnfcccVariableByIdQuery;
import moja.global.unfcccvariables.repository.updation.UpdateUnfcccVariableQuery;
import moja.global.unfcccvariables.repository.deletion.DeleteUnfcccVariablesByIdsQuery;
import moja.global.unfcccvariables.repository.insertion.InsertUnfcccVariableQuery;
import moja.global.unfcccvariables.repository.insertion.InsertUnfcccVariablesQuery;
import moja.global.unfcccvariables.repository.selection.SelectAllUnfcccVariablesQuery;
import moja.global.unfcccvariables.repository.selection.SelectUnfcccVariableByIdQuery;
import moja.global.unfcccvariables.repository.selection.SelectUnfcccVariablesByIdsQuery;
import moja.global.unfcccvariables.repository.updation.UpdateUnfcccVariablesQuery;
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
public class UnfcccVariablesRepository {

	
	@Autowired
	InsertUnfcccVariableQuery insertUnfcccVariableQuery;
	
	@Autowired
	InsertUnfcccVariablesQuery insertUnfcccVariablesQuery;
	
	@Autowired
	SelectUnfcccVariableByIdQuery selectUnfcccVariableByIdQuery;
	
	@Autowired
	SelectUnfcccVariablesByIdsQuery selectUnfcccVariablesByIdsQuery;
	
	@Autowired
	SelectAllUnfcccVariablesQuery selectAllUnfcccVariablesQuery;

	@Autowired
	UpdateUnfcccVariableQuery updateUnfcccVariableQuery;
	
	@Autowired
	UpdateUnfcccVariablesQuery updateUnfcccVariablesQuery;
	
	@Autowired
	DeleteUnfcccVariableByIdQuery deleteUnfcccVariableByIdQuery;
	
	@Autowired
	DeleteUnfcccVariablesByIdsQuery deleteUnfcccVariablesByIdsQuery;
	
	@Autowired
	DeleteAllUnfcccVariablesQuery deleteAllUnfcccVariablesQuery;


	public Mono<Long> insertUnfcccVariable(UnfcccVariable unfcccVariable) {
		return insertUnfcccVariableQuery.insertUnfcccVariable(unfcccVariable);
	}
	
	public Flux<Long> insertUnfcccVariables(UnfcccVariable[] unfcccVariables) {
		return insertUnfcccVariablesQuery.insertUnfcccVariables(unfcccVariables);
	}

	public Mono<UnfcccVariable> selectUnfcccVariableById(Long id) {
		return selectUnfcccVariableByIdQuery.selectUnfcccVariableById(id);
	}
	
	public Flux<UnfcccVariable> selectUnfcccVariablesByIds(Long[] ids) {
		return selectUnfcccVariablesByIdsQuery.selectUnfcccVariablesByIds(ids);
	}

	public Flux<UnfcccVariable> selectAllUnfcccVariables() {
		return selectAllUnfcccVariablesQuery.selectAllUnfcccVariables();
	}	

	public Mono<Integer> updateUnfcccVariable(UnfcccVariable unfcccVariable) {
		return updateUnfcccVariableQuery.updateUnfcccVariable(unfcccVariable);
	}
	
	public Flux<Integer> updateUnfcccVariables(UnfcccVariable[] unfcccVariables) {
		return updateUnfcccVariablesQuery.updateUnfcccVariables(unfcccVariables);
	}	
	
	public Mono<Integer> deleteUnfcccVariableById(Long id) {
		return deleteUnfcccVariableByIdQuery.deleteUnfcccVariableById(id);
	}
	
	public Mono<Integer> deleteUnfcccVariablesByIds(Long[] ids) {
		return deleteUnfcccVariablesByIdsQuery.deleteUnfcccVariablesByIds(ids);
	}

	public Mono<Integer> deleteAllUnfcccVariables() {
		return deleteAllUnfcccVariablesQuery.deleteAllUnfcccVariables();
	}	

}
