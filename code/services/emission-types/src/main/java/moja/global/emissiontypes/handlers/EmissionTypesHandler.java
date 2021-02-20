/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.handlers;

import moja.global.emissiontypes.handlers.post.CreateEmissionTypesHandler;
import moja.global.emissiontypes.handlers.delete.DeleteEmissionTypeHandler;
import moja.global.emissiontypes.handlers.delete.DeleteEmissionTypesHandler;
import moja.global.emissiontypes.handlers.get.RetrieveEmissionTypeHandler;
import moja.global.emissiontypes.handlers.get.RetrieveEmissionTypesHandler;
import moja.global.emissiontypes.handlers.post.CreateEmissionTypeHandler;
import moja.global.emissiontypes.handlers.put.UpdateEmissionTypeHandler;
import moja.global.emissiontypes.handlers.put.UpdateEmissionTypesHandler;
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
public class EmissionTypesHandler {

	// POST HANDLERS
	@Autowired
	CreateEmissionTypeHandler createEmissionTypeHandler;

	@Autowired
	CreateEmissionTypesHandler createEmissionTypesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveEmissionTypeHandler retrieveEmissionTypeByIdHandler;
	
	@Autowired
	RetrieveEmissionTypesHandler retrieveEmissionTypesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateEmissionTypeHandler updateEmissionTypeHandler;
	
	@Autowired
	UpdateEmissionTypesHandler updateEmissionTypesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteEmissionTypeHandler deleteEmissionTypeByIdHandler;
	
	@Autowired
	DeleteEmissionTypesHandler deleteEmissionTypesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createEmissionType(ServerRequest request) {
		return this.createEmissionTypeHandler.createEmissionType(request);
	}
	
	public Mono<ServerResponse> createEmissionTypes(ServerRequest request) {
		return createEmissionTypesHandler.createEmissionTypes(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveEmissionType(ServerRequest request) {
		return this.retrieveEmissionTypeByIdHandler.retrieveEmissionType(request);
	}
	
	public Mono<ServerResponse> retrieveEmissionTypes(ServerRequest request) {
		return this.retrieveEmissionTypesHandler.retrieveEmissionTypes(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateEmissionType(ServerRequest request) {
		return this.updateEmissionTypeHandler.updateEmissionType(request);
	}
	
	public Mono<ServerResponse> updateEmissionTypes(ServerRequest request) {
		return this.updateEmissionTypesHandler.updateEmissionTypes(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteEmissionType(ServerRequest request) {
		return this.deleteEmissionTypeByIdHandler.deleteEmissionType(request);
	}
	
	public Mono<ServerResponse> deleteEmissionTypes(ServerRequest request) {
		return this.deleteEmissionTypesHandler.deleteEmissionTypes(request);
	}	

	// </editor-fold>	

}
