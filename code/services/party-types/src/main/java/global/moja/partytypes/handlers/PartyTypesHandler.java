/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers;

import global.moja.partytypes.handlers.get.RetrievePartyTypeHandler;
import global.moja.partytypes.handlers.get.RetrievePartyTypesHandler;
import global.moja.partytypes.handlers.post.CreatePartyTypeHandler;
import global.moja.partytypes.handlers.put.UpdatePartyTypeHandler;
import global.moja.partytypes.handlers.put.UpdatePartyTypesHandler;
import global.moja.partytypes.handlers.delete.DeletePartyTypeHandler;
import global.moja.partytypes.handlers.delete.DeletePartyTypesHandler;
import global.moja.partytypes.handlers.post.CreatePartyTypesHandler;
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
public class PartyTypesHandler {

	// POST HANDLERS
	@Autowired
    CreatePartyTypeHandler createPartyTypeHandler;

	@Autowired
	CreatePartyTypesHandler createPartyTypesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrievePartyTypeHandler retrievePartyTypeByIdHandler;
	
	@Autowired
    RetrievePartyTypesHandler retrievePartyTypesHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdatePartyTypeHandler updatePartyTypeHandler;
	
	@Autowired
    UpdatePartyTypesHandler updatePartyTypesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeletePartyTypeHandler deletePartyTypeByIdHandler;
	
	@Autowired
	DeletePartyTypesHandler deletePartyTypesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createPartyType(ServerRequest request) {
		return this.createPartyTypeHandler.createPartyType(request);
	}
	
	public Mono<ServerResponse> createPartyTypes(ServerRequest request) {
		return createPartyTypesHandler.createPartyTypes(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrievePartyType(ServerRequest request) {
		return this.retrievePartyTypeByIdHandler.retrievePartyType(request);
	}
	
	public Mono<ServerResponse> retrievePartyTypes(ServerRequest request) {
		return this.retrievePartyTypesHandler.retrievePartyTypes(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updatePartyType(ServerRequest request) {
		return this.updatePartyTypeHandler.updatePartyType(request);
	}
	
	public Mono<ServerResponse> updatePartyTypes(ServerRequest request) {
		return this.updatePartyTypesHandler.updatePartyTypes(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deletePartyType(ServerRequest request) {
		return this.deletePartyTypeByIdHandler.deletePartyType(request);
	}
	
	public Mono<ServerResponse> deletePartyTypes(ServerRequest request) {
		return this.deletePartyTypesHandler.deletePartyTypes(request);
	}	

	// </editor-fold>	

}
