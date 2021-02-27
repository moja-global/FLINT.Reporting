/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.handlers;

import global.moja.reportingvariables.handlers.delete.DeleteReportingVariableHandler;
import global.moja.reportingvariables.handlers.delete.DeleteReportingVariablesHandler;
import global.moja.reportingvariables.handlers.get.RetrieveReportingVariableHandler;
import global.moja.reportingvariables.handlers.get.RetrieveReportingVariablesHandler;
import global.moja.reportingvariables.handlers.post.CreateReportingVariableHandler;
import global.moja.reportingvariables.handlers.post.CreateReportingVariablesHandler;
import global.moja.reportingvariables.handlers.put.UpdateReportingVariableHandler;
import global.moja.reportingvariables.handlers.put.UpdateReportingVariablesHandler;
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
public class ReportingVariablesHandler {

	// POST HANDLERS
	@Autowired
	CreateReportingVariableHandler createReportingVariableHandler;

	@Autowired
	CreateReportingVariablesHandler createReportingVariablesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveReportingVariableHandler retrieveReportingVariableByIdHandler;
	
	@Autowired
	RetrieveReportingVariablesHandler retrieveReportingVariablesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateReportingVariableHandler updateReportingVariableHandler;
	
	@Autowired
	UpdateReportingVariablesHandler updateReportingVariablesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteReportingVariableHandler deleteReportingVariableByIdHandler;
	
	@Autowired
	DeleteReportingVariablesHandler deleteReportingVariablesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createReportingVariable(ServerRequest request) {
		return this.createReportingVariableHandler.createReportingVariable(request);
	}
	
	public Mono<ServerResponse> createReportingVariables(ServerRequest request) {
		return createReportingVariablesHandler.createReportingVariables(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveReportingVariable(ServerRequest request) {
		return this.retrieveReportingVariableByIdHandler.retrieveReportingVariable(request);
	}
	
	public Mono<ServerResponse> retrieveReportingVariables(ServerRequest request) {
		return this.retrieveReportingVariablesHandler.retrieveReportingVariables(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateReportingVariable(ServerRequest request) {
		return this.updateReportingVariableHandler.updateReportingVariable(request);
	}
	
	public Mono<ServerResponse> updateReportingVariables(ServerRequest request) {
		return this.updateReportingVariablesHandler.updateReportingVariables(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteReportingVariable(ServerRequest request) {
		return this.deleteReportingVariableByIdHandler.deleteReportingVariable(request);
	}
	
	public Mono<ServerResponse> deleteReportingVariables(ServerRequest request) {
		return this.deleteReportingVariablesHandler.deleteReportingVariables(request);
	}	

	// </editor-fold>	

}
