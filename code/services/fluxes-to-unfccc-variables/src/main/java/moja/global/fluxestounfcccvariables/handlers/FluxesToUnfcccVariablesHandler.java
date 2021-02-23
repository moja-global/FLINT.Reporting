/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.handlers;

import moja.global.fluxestounfcccvariables.handlers.post.CreateFluxesToUnfcccVariablesHandler;
import moja.global.fluxestounfcccvariables.handlers.delete.DeleteFluxToUnfcccVariableHandler;
import moja.global.fluxestounfcccvariables.handlers.delete.DeleteFluxesToUnfcccVariablesHandler;
import moja.global.fluxestounfcccvariables.handlers.get.RetrieveFluxToUnfcccVariableHandler;
import moja.global.fluxestounfcccvariables.handlers.get.RetrieveFluxesToUnfcccVariablesHandler;
import moja.global.fluxestounfcccvariables.handlers.post.CreateFluxToUnfcccVariableHandler;
import moja.global.fluxestounfcccvariables.handlers.put.UpdateFluxToUnfcccVariableHandler;
import moja.global.fluxestounfcccvariables.handlers.put.UpdateFluxesToUnfcccVariablesHandler;
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
public class FluxesToUnfcccVariablesHandler {

	// POST HANDLERS
	@Autowired
	CreateFluxToUnfcccVariableHandler createFluxToUnfcccVariableHandler;

	@Autowired
	CreateFluxesToUnfcccVariablesHandler createFluxesToUnfcccVariablesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveFluxToUnfcccVariableHandler retrieveFluxToUnfcccVariableByIdHandler;
	
	@Autowired
	RetrieveFluxesToUnfcccVariablesHandler retrieveFluxesToUnfcccVariablesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateFluxToUnfcccVariableHandler updateFluxToUnfcccVariableHandler;
	
	@Autowired
	UpdateFluxesToUnfcccVariablesHandler updateFluxesToUnfcccVariablesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteFluxToUnfcccVariableHandler deleteFluxToUnfcccVariableByIdHandler;
	
	@Autowired
	DeleteFluxesToUnfcccVariablesHandler deleteFluxesToUnfcccVariablesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createFluxToUnfcccVariable(ServerRequest request) {
		return this.createFluxToUnfcccVariableHandler.createFluxToUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> createFluxesToUnfcccVariables(ServerRequest request) {
		return createFluxesToUnfcccVariablesHandler.createFluxesToUnfcccVariables(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveFluxToUnfcccVariable(ServerRequest request) {
		return this.retrieveFluxToUnfcccVariableByIdHandler.retrieveFluxToUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> retrieveFluxesToUnfcccVariables(ServerRequest request) {
		return this.retrieveFluxesToUnfcccVariablesHandler.retrieveFluxesToUnfcccVariables(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateFluxToUnfcccVariable(ServerRequest request) {
		return this.updateFluxToUnfcccVariableHandler.updateFluxToUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> updateFluxesToUnfcccVariables(ServerRequest request) {
		return this.updateFluxesToUnfcccVariablesHandler.updateFluxesToUnfcccVariables(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteFluxToUnfcccVariable(ServerRequest request) {
		return this.deleteFluxToUnfcccVariableByIdHandler.deleteFluxToUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> deleteFluxesToUnfcccVariables(ServerRequest request) {
		return this.deleteFluxesToUnfcccVariablesHandler.deleteFluxesToUnfcccVariables(request);
	}	

	// </editor-fold>	

}
