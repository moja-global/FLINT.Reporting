/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.handlers;

import moja.global.reportingtables.handlers.post.CreateReportingTablesHandler;
import moja.global.reportingtables.handlers.delete.DeleteReportingTableHandler;
import moja.global.reportingtables.handlers.delete.DeleteReportingTablesHandler;
import moja.global.reportingtables.handlers.get.RetrieveReportingTableHandler;
import moja.global.reportingtables.handlers.get.RetrieveReportingTablesHandler;
import moja.global.reportingtables.handlers.post.CreateReportingTableHandler;
import moja.global.reportingtables.handlers.put.UpdateReportingTableHandler;
import moja.global.reportingtables.handlers.put.UpdateReportingTablesHandler;
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
public class ReportingTablesHandler {

	// POST HANDLERS
	@Autowired
	CreateReportingTableHandler createReportingTableHandler;

	@Autowired
	CreateReportingTablesHandler createReportingTablesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveReportingTableHandler retrieveReportingTableByIdHandler;
	
	@Autowired
	RetrieveReportingTablesHandler retrieveReportingTablesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateReportingTableHandler updateReportingTableHandler;
	
	@Autowired
	UpdateReportingTablesHandler updateReportingTablesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteReportingTableHandler deleteReportingTableByIdHandler;
	
	@Autowired
	DeleteReportingTablesHandler deleteReportingTablesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createReportingTable(ServerRequest request) {
		return this.createReportingTableHandler.createReportingTable(request);
	}
	
	public Mono<ServerResponse> createReportingTables(ServerRequest request) {
		return createReportingTablesHandler.createReportingTables(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveReportingTable(ServerRequest request) {
		return this.retrieveReportingTableByIdHandler.retrieveReportingTable(request);
	}
	
	public Mono<ServerResponse> retrieveReportingTables(ServerRequest request) {
		return this.retrieveReportingTablesHandler.retrieveReportingTables(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateReportingTable(ServerRequest request) {
		return this.updateReportingTableHandler.updateReportingTable(request);
	}
	
	public Mono<ServerResponse> updateReportingTables(ServerRequest request) {
		return this.updateReportingTablesHandler.updateReportingTables(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteReportingTable(ServerRequest request) {
		return this.deleteReportingTableByIdHandler.deleteReportingTable(request);
	}
	
	public Mono<ServerResponse> deleteReportingTables(ServerRequest request) {
		return this.deleteReportingTablesHandler.deleteReportingTables(request);
	}	

	// </editor-fold>	

}
