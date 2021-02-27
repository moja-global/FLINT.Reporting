/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.repository;

import global.moja.reportingvariables.daos.QueryParameters;
import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.repository.updation.UpdateReportingVariableQuery;
import global.moja.reportingvariables.repository.updation.UpdateReportingVariablesQuery;
import global.moja.reportingvariables.repository.deletion.DeleteReportingVariableQuery;
import global.moja.reportingvariables.repository.deletion.DeleteReportingVariablesQuery;
import global.moja.reportingvariables.repository.selection.SelectReportingVariablesQuery;
import global.moja.reportingvariables.repository.insertion.InsertReportingVariableQuery;
import global.moja.reportingvariables.repository.insertion.InsertReportingVariablesQuery;
import global.moja.reportingvariables.repository.selection.SelectReportingVariableQuery;
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
public class ReportingVariablesRepository {

	
	@Autowired
	InsertReportingVariableQuery insertReportingVariableQuery;
	
	@Autowired
	InsertReportingVariablesQuery insertReportingVariablesQuery;
	
	@Autowired
	SelectReportingVariableQuery selectReportingVariableQuery;
	
	@Autowired
	SelectReportingVariablesQuery selectReportingVariablesQuery;

	@Autowired
	UpdateReportingVariableQuery updateReportingVariableQuery;
	
	@Autowired
	UpdateReportingVariablesQuery updateReportingVariablesQuery;
	
	@Autowired
	DeleteReportingVariableQuery deleteReportingVariableQuery;
	
	@Autowired
    DeleteReportingVariablesQuery deleteReportingVariablesQuery;


	public Mono<Long> insertReportingVariable(ReportingVariable reportingVariable) {
		return insertReportingVariableQuery.insertReportingVariable(reportingVariable);
	}
	
	public Flux<Long> insertReportingVariables(ReportingVariable[] reportingVariables) {
		return insertReportingVariablesQuery.insertReportingVariables(reportingVariables);
	}

	public Mono<ReportingVariable> selectReportingVariable(Long id) {
		return selectReportingVariableQuery.selectReportingVariable(id);
	}
	
	public Flux<ReportingVariable> selectReportingVariables(QueryParameters parameters) {
		return selectReportingVariablesQuery.selectReportingVariables(parameters);
	}

	public Mono<Integer> updateReportingVariable(ReportingVariable reportingVariable) {
		return updateReportingVariableQuery.updateReportingVariable(reportingVariable);
	}
	
	public Flux<Integer> updateReportingVariables(ReportingVariable[] reportingVariables) {
		return updateReportingVariablesQuery.updateReportingVariables(reportingVariables);
	}	
	
	public Mono<Integer> deleteReportingVariableById(Long id) {
		return deleteReportingVariableQuery.deleteReportingVariable(id);
	}
	
	public Mono<Integer> deleteReportingVariables(QueryParameters parameters) {
		return deleteReportingVariablesQuery.deleteReportingVariables(parameters);
	}


}
