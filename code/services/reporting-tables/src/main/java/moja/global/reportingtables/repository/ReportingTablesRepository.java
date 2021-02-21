/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.repository;

import moja.global.reportingtables.models.ReportingTable;
import moja.global.reportingtables.repository.deletion.DeleteAllReportingTablesQuery;
import moja.global.reportingtables.repository.deletion.DeleteReportingTableByIdQuery;
import moja.global.reportingtables.repository.updation.UpdateReportingTableQuery;
import moja.global.reportingtables.repository.deletion.DeleteReportingTablesByIdsQuery;
import moja.global.reportingtables.repository.insertion.InsertReportingTableQuery;
import moja.global.reportingtables.repository.insertion.InsertReportingTablesQuery;
import moja.global.reportingtables.repository.selection.SelectAllReportingTablesQuery;
import moja.global.reportingtables.repository.selection.SelectReportingTableByIdQuery;
import moja.global.reportingtables.repository.selection.SelectReportingTablesByIdsQuery;
import moja.global.reportingtables.repository.updation.UpdateReportingTablesQuery;
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
	SelectReportingTableByIdQuery selectReportingTableByIdQuery;
	
	@Autowired
	SelectReportingTablesByIdsQuery selectReportingTablesByIdsQuery;
	
	@Autowired
	SelectAllReportingTablesQuery selectAllReportingTablesQuery;

	@Autowired
	UpdateReportingTableQuery updateReportingTableQuery;
	
	@Autowired
	UpdateReportingTablesQuery updateReportingTablesQuery;
	
	@Autowired
	DeleteReportingTableByIdQuery deleteReportingTableByIdQuery;
	
	@Autowired
	DeleteReportingTablesByIdsQuery deleteReportingTablesByIdsQuery;
	
	@Autowired
	DeleteAllReportingTablesQuery deleteAllReportingTablesQuery;


	public Mono<Long> insertReportingTable(ReportingTable reportingTable) {
		return insertReportingTableQuery.insertReportingTable(reportingTable);
	}
	
	public Flux<Long> insertReportingTables(ReportingTable[] reportingTables) {
		return insertReportingTablesQuery.insertReportingTables(reportingTables);
	}

	public Mono<ReportingTable> selectReportingTableById(Long id) {
		return selectReportingTableByIdQuery.selectReportingTableById(id);
	}
	
	public Flux<ReportingTable> selectReportingTablesByIds(Long[] ids) {
		return selectReportingTablesByIdsQuery.selectReportingTablesByIds(ids);
	}

	public Flux<ReportingTable> selectAllReportingTables() {
		return selectAllReportingTablesQuery.selectAllReportingTables();
	}	

	public Mono<Integer> updateReportingTable(ReportingTable reportingTable) {
		return updateReportingTableQuery.updateReportingTable(reportingTable);
	}
	
	public Flux<Integer> updateReportingTables(ReportingTable[] reportingTables) {
		return updateReportingTablesQuery.updateReportingTables(reportingTables);
	}	
	
	public Mono<Integer> deleteReportingTableById(Long id) {
		return deleteReportingTableByIdQuery.deleteReportingTableById(id);
	}
	
	public Mono<Integer> deleteReportingTablesByIds(Long[] ids) {
		return deleteReportingTablesByIdsQuery.deleteReportingTablesByIds(ids);
	}

	public Mono<Integer> deleteAllReportingTables() {
		return deleteAllReportingTablesQuery.deleteAllReportingTables();
	}	

}
