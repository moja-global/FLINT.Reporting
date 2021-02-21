/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.handlers;

import moja.global.unfcccvariables.handlers.post.CreateUnfcccVariablesHandler;
import moja.global.unfcccvariables.handlers.delete.DeleteUnfcccVariableHandler;
import moja.global.unfcccvariables.handlers.delete.DeleteUnfcccVariablesHandler;
import moja.global.unfcccvariables.handlers.get.RetrieveUnfcccVariableHandler;
import moja.global.unfcccvariables.handlers.get.RetrieveUnfcccVariablesHandler;
import moja.global.unfcccvariables.handlers.post.CreateUnfcccVariableHandler;
import moja.global.unfcccvariables.handlers.put.UpdateUnfcccVariableHandler;
import moja.global.unfcccvariables.handlers.put.UpdateUnfcccVariablesHandler;
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
public class UnfcccVariablesHandler {

	// POST HANDLERS
	@Autowired
	CreateUnfcccVariableHandler createUnfcccVariableHandler;

	@Autowired
	CreateUnfcccVariablesHandler createUnfcccVariablesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveUnfcccVariableHandler retrieveUnfcccVariableByIdHandler;
	
	@Autowired
	RetrieveUnfcccVariablesHandler retrieveUnfcccVariablesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateUnfcccVariableHandler updateUnfcccVariableHandler;
	
	@Autowired
	UpdateUnfcccVariablesHandler updateUnfcccVariablesHandler;

	
	// DELETE HANDLERS
	@Autowired
    DeleteUnfcccVariableHandler deleteUnfcccVariableByIdHandler;
	
	@Autowired
	DeleteUnfcccVariablesHandler deleteUnfcccVariablesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createUnfcccVariable(ServerRequest request) {
		return this.createUnfcccVariableHandler.createUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> createUnfcccVariables(ServerRequest request) {
		return createUnfcccVariablesHandler.createUnfcccVariables(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveUnfcccVariable(ServerRequest request) {
		return this.retrieveUnfcccVariableByIdHandler.retrieveUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> retrieveUnfcccVariables(ServerRequest request) {
		return this.retrieveUnfcccVariablesHandler.retrieveUnfcccVariables(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateUnfcccVariable(ServerRequest request) {
		return this.updateUnfcccVariableHandler.updateUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> updateUnfcccVariables(ServerRequest request) {
		return this.updateUnfcccVariablesHandler.updateUnfcccVariables(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteUnfcccVariable(ServerRequest request) {
		return this.deleteUnfcccVariableByIdHandler.deleteUnfcccVariable(request);
	}
	
	public Mono<ServerResponse> deleteUnfcccVariables(ServerRequest request) {
		return this.deleteUnfcccVariablesHandler.deleteUnfcccVariables(request);
	}	

	// </editor-fold>	

}
