/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.handlers;

import global.moja.landusesfluxtypestoreportingtables.handlers.delete.DeleteLandUseFluxTypeToReportingTableHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.delete.DeleteLandUsesFluxTypesToReportingTablesHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.get.RetrieveLandUseFluxTypeToReportingTableHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.get.RetrieveLandUsesFluxTypesToReportingTablesHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.post.CreateLandUseFluxTypeToReportingTableHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.post.CreateLandUsesFluxTypesToReportingTablesHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.put.UpdateLandUseFluxTypeToReportingTableHandler;
import global.moja.landusesfluxtypestoreportingtables.handlers.put.UpdateLandUsesFluxTypesToReportingTablesHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class LandUsesFluxTypesToReportingTablesHandler {

	// POST HANDLERS
	@Autowired
    CreateLandUseFluxTypeToReportingTableHandler createLandUseFluxTypeToReportingTableHandler;

	@Autowired
    CreateLandUsesFluxTypesToReportingTablesHandler createLandUsesFluxTypesToReportingTablesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveLandUseFluxTypeToReportingTableHandler retrieveLandUseFluxTypeToReportingTableByIdHandler;
	
	@Autowired
    RetrieveLandUsesFluxTypesToReportingTablesHandler retrieveLandUsesFluxTypesToReportingTablesHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdateLandUseFluxTypeToReportingTableHandler updateLandUseFluxTypeToReportingTableHandler;
	
	@Autowired
    UpdateLandUsesFluxTypesToReportingTablesHandler updateLandUsesFluxTypesToReportingTablesHandler;

	
	// DELETE HANDLERS
	@Autowired
    DeleteLandUseFluxTypeToReportingTableHandler deleteLandUseFluxTypeToReportingTableByIdHandler;
	
	@Autowired
    DeleteLandUsesFluxTypesToReportingTablesHandler deleteLandUsesFluxTypesToReportingTablesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createLandUseFluxTypeToReportingTable(ServerRequest request) {
		return this.createLandUseFluxTypeToReportingTableHandler.createLandUseFluxTypeToReportingTable(request);
	}
	
	public Mono<ServerResponse> createLandUsesFluxTypesToReportingTables(ServerRequest request) {
		return createLandUsesFluxTypesToReportingTablesHandler.createLandUsesFluxTypesToReportingTables(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveLandUseFluxTypeToReportingTable(ServerRequest request) {
		return this.retrieveLandUseFluxTypeToReportingTableByIdHandler.retrieveLandUseFluxTypeToReportingTable(request);
	}
	
	public Mono<ServerResponse> retrieveLandUsesFluxTypesToReportingTables(ServerRequest request) {
		return this.retrieveLandUsesFluxTypesToReportingTablesHandler.retrieveLandUsesFluxTypesToReportingTables(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateLandUseFluxTypeToReportingTable(ServerRequest request) {
		return this.updateLandUseFluxTypeToReportingTableHandler.updateLandUseFluxTypeToReportingTable(request);
	}
	
	public Mono<ServerResponse> updateLandUsesFluxTypesToReportingTables(ServerRequest request) {
		return this.updateLandUsesFluxTypesToReportingTablesHandler.updateLandUsesFluxTypesToReportingTables(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteLandUseFluxTypeToReportingTable(ServerRequest request) {
		return this.deleteLandUseFluxTypeToReportingTableByIdHandler.deleteLandUseFluxTypeToReportingTable(request);
	}
	
	public Mono<ServerResponse> deleteLandUsesFluxTypesToReportingTables(ServerRequest request) {
		return this.deleteLandUsesFluxTypesToReportingTablesHandler.deleteLandUsesFluxTypesToReportingTables(request);
	}	

	// </editor-fold>	

}
