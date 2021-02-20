/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes.handlers;

import moja.global.fluxtypes.handlers.post.CreateFluxTypesHandler;
import moja.global.fluxtypes.handlers.delete.DeleteFluxTypeHandler;
import moja.global.fluxtypes.handlers.delete.DeleteFluxTypesHandler;
import moja.global.fluxtypes.handlers.get.RetrieveFluxTypeHandler;
import moja.global.fluxtypes.handlers.get.RetrieveFluxTypesHandler;
import moja.global.fluxtypes.handlers.post.CreateFluxTypeHandler;
import moja.global.fluxtypes.handlers.put.UpdateFluxTypeHandler;
import moja.global.fluxtypes.handlers.put.UpdateFluxTypesHandler;
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
public class FluxTypesHandler {

	// POST HANDLERS
	@Autowired
	CreateFluxTypeHandler createFluxTypeHandler;

	@Autowired
    CreateFluxTypesHandler createFluxTypesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveFluxTypeHandler retrieveFluxTypeByIdHandler;
	
	@Autowired
	RetrieveFluxTypesHandler retrieveFluxTypesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateFluxTypeHandler updateFluxTypeHandler;
	
	@Autowired
	UpdateFluxTypesHandler updateFluxTypesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteFluxTypeHandler deleteFluxTypeByIdHandler;
	
	@Autowired
	DeleteFluxTypesHandler deleteFluxTypesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createFluxType(ServerRequest request) {
		return this.createFluxTypeHandler.createFluxType(request);
	}
	
	public Mono<ServerResponse> createFluxTypes(ServerRequest request) {
		return createFluxTypesHandler.createFluxTypes(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveFluxType(ServerRequest request) {
		return this.retrieveFluxTypeByIdHandler.retrieveFluxType(request);
	}
	
	public Mono<ServerResponse> retrieveFluxTypes(ServerRequest request) {
		return this.retrieveFluxTypesHandler.retrieveFluxTypes(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateFluxType(ServerRequest request) {
		return this.updateFluxTypeHandler.updateFluxType(request);
	}
	
	public Mono<ServerResponse> updateFluxTypes(ServerRequest request) {
		return this.updateFluxTypesHandler.updateFluxTypes(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteFluxType(ServerRequest request) {
		return this.deleteFluxTypeByIdHandler.deleteFluxType(request);
	}
	
	public Mono<ServerResponse> deleteFluxTypes(ServerRequest request) {
		return this.deleteFluxTypesHandler.deleteFluxTypes(request);
	}	

	// </editor-fold>	

}
