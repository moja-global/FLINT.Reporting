/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.repository;

import global.moja.reportingtable.daos.QueryParameters;
import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.repository.deletion.DeleteReportingTableQuery;
import global.moja.reportingtable.repository.deletion.DeleteReportingTablesQuery;
import global.moja.reportingtable.repository.updation.UpdateReportingTableQuery;
import global.moja.reportingtable.repository.updation.UpdateReportingTablesQuery;
import global.moja.reportingtable.repository.selection.SelectReportingTablesQuery;
import global.moja.reportingtable.repository.insertion.InsertReportingTableQuery;
import global.moja.reportingtable.repository.insertion.InsertReportingTablesQuery;
import global.moja.reportingtable.repository.selection.SelectReportingTableQuery;
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
public class ReportingTablesRepository {

	
	@Autowired
	InsertReportingTableQuery insertReportingTableQuery;
	
	@Autowired
	InsertReportingTablesQuery insertReportingTablesQuery;
	
	@Autowired
	SelectReportingTableQuery selectReportingTableQuery;
	
	@Autowired
	SelectReportingTablesQuery selectReportingTablesQuery;

	@Autowired
	UpdateReportingTableQuery updateReportingTableQuery;
	
	@Autowired
	UpdateReportingTablesQuery updateReportingTablesQuery;
	
	@Autowired
    DeleteReportingTableQuery deleteReportingTableQuery;
	
	@Autowired
    DeleteReportingTablesQuery deleteReportingTablesQuery;


	public Mono<Long> insertReportingTable(ReportingTable reportingTable) {
		return insertReportingTableQuery.insertReportingTable(reportingTable);
	}
	
	public Flux<Long> insertReportingTables(ReportingTable[] reportingTables) {
		return insertReportingTablesQuery.insertReportingTables(reportingTables);
	}

	public Mono<ReportingTable> selectReportingTable(Long id) {
		return selectReportingTableQuery.selectReportingTable(id);
	}
	
	public Flux<ReportingTable> selectReportingTables(QueryParameters parameters) {
		return selectReportingTablesQuery.selectReportingTables(parameters);
	}

	public Mono<Integer> updateReportingTable(ReportingTable reportingTable) {
		return updateReportingTableQuery.updateReportingTable(reportingTable);
	}
	
	public Flux<Integer> updateReportingTables(ReportingTable[] reportingTables) {
		return updateReportingTablesQuery.updateReportingTables(reportingTables);
	}	
	
	public Mono<Integer> deleteReportingTableById(Long id) {
		return deleteReportingTableQuery.deleteReportingTable(id);
	}
	
	public Mono<Integer> deleteReportingTables(QueryParameters parameters) {
		return deleteReportingTablesQuery.deleteReportingTables(parameters);
	}


}
