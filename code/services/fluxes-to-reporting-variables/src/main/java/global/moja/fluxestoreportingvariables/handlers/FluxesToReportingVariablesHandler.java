/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.handlers;

import global.moja.fluxestoreportingvariables.handlers.put.UpdateFluxToReportingVariableHandler;
import global.moja.fluxestoreportingvariables.handlers.post.CreateFluxesToReportingVariablesHandler;
import global.moja.fluxestoreportingvariables.handlers.delete.DeleteFluxToReportingVariableHandler;
import global.moja.fluxestoreportingvariables.handlers.delete.DeleteFluxesToReportingVariablesHandler;
import global.moja.fluxestoreportingvariables.handlers.get.RetrieveFluxToReportingVariableHandler;
import global.moja.fluxestoreportingvariables.handlers.get.RetrieveFluxesToReportingVariablesHandler;
import global.moja.fluxestoreportingvariables.handlers.post.CreateFluxToReportingVariableHandler;
import global.moja.fluxestoreportingvariables.handlers.put.UpdateFluxesToReportingVariablesHandler;
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
public class FluxesToReportingVariablesHandler {

	// POST HANDLERS
	@Autowired
	CreateFluxToReportingVariableHandler createFluxToReportingVariableHandler;

	@Autowired
	CreateFluxesToReportingVariablesHandler createFluxesToReportingVariablesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveFluxToReportingVariableHandler retrieveFluxToReportingVariableByIdHandler;
	
	@Autowired
	RetrieveFluxesToReportingVariablesHandler retrieveFluxesToReportingVariablesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateFluxToReportingVariableHandler updateFluxToReportingVariableHandler;
	
	@Autowired
	UpdateFluxesToReportingVariablesHandler updateFluxesToReportingVariablesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteFluxToReportingVariableHandler deleteFluxToReportingVariableByIdHandler;
	
	@Autowired
	DeleteFluxesToReportingVariablesHandler deleteFluxesToReportingVariablesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createFluxToReportingVariable(ServerRequest request) {
		return this.createFluxToReportingVariableHandler.createFluxToReportingVariable(request);
	}
	
	public Mono<ServerResponse> createFluxesToReportingVariables(ServerRequest request) {
		return createFluxesToReportingVariablesHandler.createFluxesToReportingVariables(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveFluxToReportingVariable(ServerRequest request) {
		return this.retrieveFluxToReportingVariableByIdHandler.retrieveFluxToReportingVariable(request);
	}
	
	public Mono<ServerResponse> retrieveFluxesToReportingVariables(ServerRequest request) {
		return this.retrieveFluxesToReportingVariablesHandler.retrieveFluxesToReportingVariables(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateFluxToReportingVariable(ServerRequest request) {
		return this.updateFluxToReportingVariableHandler.updateFluxToReportingVariable(request);
	}
	
	public Mono<ServerResponse> updateFluxesToReportingVariables(ServerRequest request) {
		return this.updateFluxesToReportingVariablesHandler.updateFluxesToReportingVariables(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteFluxToReportingVariable(ServerRequest request) {
		return this.deleteFluxToReportingVariableByIdHandler.deleteFluxToReportingVariable(request);
	}
	
	public Mono<ServerResponse> deleteFluxesToReportingVariables(ServerRequest request) {
		return this.deleteFluxesToReportingVariablesHandler.deleteFluxesToReportingVariables(request);
	}	

	// </editor-fold>	

}
