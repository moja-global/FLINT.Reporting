/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository;

import global.moja.landusesfluxtypestoreportingtables.repository.deletion.DeleteLandUsesFluxTypesToReportingTablesQuery;
import global.moja.landusesfluxtypestoreportingtables.daos.QueryParameters;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
import global.moja.landusesfluxtypestoreportingtables.repository.updation.UpdateLandUseFluxTypeToReportingTableQuery;
import global.moja.landusesfluxtypestoreportingtables.repository.updation.UpdateLandUsesFluxTypesToReportingTablesQuery;
import global.moja.landusesfluxtypestoreportingtables.repository.deletion.DeleteLandUseFluxTypeToReportingTableQuery;
import global.moja.landusesfluxtypestoreportingtables.repository.selection.SelectLandUsesFluxTypesToReportingTablesQuery;
import global.moja.landusesfluxtypestoreportingtables.repository.insertion.InsertLandUseFluxTypeToReportingTableQuery;
import global.moja.landusesfluxtypestoreportingtables.repository.insertion.InsertLandUsesFluxTypesToReportingTablesQuery;
import global.moja.landusesfluxtypestoreportingtables.repository.selection.SelectLandUseFluxTypeToReportingTableQuery;
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
public class LandUsesFluxTypesToReportingTablesRepository {

	
	@Autowired
	InsertLandUseFluxTypeToReportingTableQuery insertLandUseFluxTypeToReportingTableQuery;
	
	@Autowired
	InsertLandUsesFluxTypesToReportingTablesQuery insertLandUsesFluxTypesToReportingTablesQuery;
	
	@Autowired
	SelectLandUseFluxTypeToReportingTableQuery selectLandUseFluxTypeToReportingTableQuery;
	
	@Autowired
	SelectLandUsesFluxTypesToReportingTablesQuery selectLandUsesFluxTypesToReportingTablesQuery;

	@Autowired
	UpdateLandUseFluxTypeToReportingTableQuery updateLandUseFluxTypeToReportingTableQuery;
	
	@Autowired
	UpdateLandUsesFluxTypesToReportingTablesQuery updateLandUsesFluxTypesToReportingTablesQuery;
	
	@Autowired
	DeleteLandUseFluxTypeToReportingTableQuery deleteLandUseFluxTypeToReportingTableQuery;
	
	@Autowired
    DeleteLandUsesFluxTypesToReportingTablesQuery deleteLandUsesFluxTypesToReportingTablesQuery;


	public Mono<Long> insertLandUseFluxTypeToReportingTable(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable) {
		return insertLandUseFluxTypeToReportingTableQuery.insertLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable);
	}
	
	public Flux<Long> insertLandUsesFluxTypesToReportingTables(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {
		return insertLandUsesFluxTypesToReportingTablesQuery.insertLandUsesFluxTypesToReportingTables(landUsesFluxTypesToReportingTables);
	}

	public Mono<LandUseFluxTypeToReportingTable> selectLandUseFluxTypeToReportingTable(Long id) {
		return selectLandUseFluxTypeToReportingTableQuery.selectLandUseFluxTypeToReportingTable(id);
	}
	
	public Flux<LandUseFluxTypeToReportingTable> selectLandUsesFluxTypesToReportingTables(QueryParameters parameters) {
		return selectLandUsesFluxTypesToReportingTablesQuery.selectLandUsesFluxTypesToReportingTables(parameters);
	}

	public Mono<Integer> updateLandUseFluxTypeToReportingTable(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable) {
		return updateLandUseFluxTypeToReportingTableQuery.updateLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable);
	}
	
	public Flux<Integer> updateLandUsesFluxTypesToReportingTables(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {
		return updateLandUsesFluxTypesToReportingTablesQuery.updateLandUsesFluxTypesToReportingTables(landUsesFluxTypesToReportingTables);
	}	
	
	public Mono<Integer> deleteLandUseFluxTypeToReportingTableById(Long id) {
		return deleteLandUseFluxTypeToReportingTableQuery.deleteLandUseFluxTypeToReportingTable(id);
	}
	
	public Mono<Integer> deleteLandUsesFluxTypesToReportingTables(QueryParameters parameters) {
		return deleteLandUsesFluxTypesToReportingTablesQuery.deleteLandUsesFluxTypesToReportingTables(parameters);
	}


}
