/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.repository;

import global.moja.reportingframework.daos.QueryParameters;
import global.moja.reportingframework.models.ReportingFramework;
import global.moja.reportingframework.repository.updation.UpdateReportingFrameworkQuery;
import global.moja.reportingframework.repository.updation.UpdateReportingFrameworksQuery;
import global.moja.reportingframework.repository.deletion.DeleteReportingFrameworkQuery;
import global.moja.reportingframework.repository.deletion.DeleteReportingFrameworksQuery;
import global.moja.reportingframework.repository.selection.SelectReportingFrameworksQuery;
import global.moja.reportingframework.repository.insertion.InsertReportingFrameworkQuery;
import global.moja.reportingframework.repository.insertion.InsertReportingFrameworksQuery;
import global.moja.reportingframework.repository.selection.SelectReportingFrameworkQuery;
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
public class ReportingFrameworksRepository {

	
	@Autowired
	InsertReportingFrameworkQuery insertReportingFrameworkQuery;
	
	@Autowired
	InsertReportingFrameworksQuery insertReportingFrameworksQuery;
	
	@Autowired
	SelectReportingFrameworkQuery selectReportingFrameworkQuery;
	
	@Autowired
	SelectReportingFrameworksQuery selectReportingFrameworksQuery;

	@Autowired
	UpdateReportingFrameworkQuery updateReportingFrameworkQuery;
	
	@Autowired
	UpdateReportingFrameworksQuery updateReportingFrameworksQuery;
	
	@Autowired
	DeleteReportingFrameworkQuery deleteReportingFrameworkQuery;
	
	@Autowired
    DeleteReportingFrameworksQuery deleteReportingFrameworksQuery;


	public Mono<Long> insertReportingFramework(ReportingFramework reportingFramework) {
		return insertReportingFrameworkQuery.insertReportingFramework(reportingFramework);
	}
	
	public Flux<Long> insertReportingFrameworks(ReportingFramework[] reportingFrameworks) {
		return insertReportingFrameworksQuery.insertReportingFrameworks(reportingFrameworks);
	}

	public Mono<ReportingFramework> selectReportingFramework(Long id) {
		return selectReportingFrameworkQuery.selectReportingFramework(id);
	}
	
	public Flux<ReportingFramework> selectReportingFrameworks(QueryParameters parameters) {
		return selectReportingFrameworksQuery.selectReportingFrameworks(parameters);
	}

	public Mono<Integer> updateReportingFramework(ReportingFramework reportingFramework) {
		return updateReportingFrameworkQuery.updateReportingFramework(reportingFramework);
	}
	
	public Flux<Integer> updateReportingFrameworks(ReportingFramework[] reportingFrameworks) {
		return updateReportingFrameworksQuery.updateReportingFrameworks(reportingFrameworks);
	}	
	
	public Mono<Integer> deleteReportingFrameworkById(Long id) {
		return deleteReportingFrameworkQuery.deleteReportingFramework(id);
	}
	
	public Mono<Integer> deleteReportingFrameworks(QueryParameters parameters) {
		return deleteReportingFrameworksQuery.deleteReportingFrameworks(parameters);
	}


}
