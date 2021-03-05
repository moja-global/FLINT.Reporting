/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.handlers;

import global.moja.parties.handlers.delete.DeletePartyHandler;
import global.moja.parties.handlers.post.CreatePartiesHandler;
import global.moja.parties.handlers.post.CreatePartyHandler;
import global.moja.parties.handlers.delete.DeletePartiesHandler;
import global.moja.parties.handlers.get.RetrievePartiesHandler;
import global.moja.parties.handlers.put.UpdatePartyHandler;
import global.moja.parties.handlers.put.UpdatePartiesHandler;
import global.moja.parties.handlers.get.RetrievePartyHandler;
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
public class PartiesHandler {

	// POST HANDLERS
	@Autowired
	CreatePartyHandler createPartyHandler;

	@Autowired
	CreatePartiesHandler createPartiesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrievePartyHandler retrievePartyByIdHandler;
	
	@Autowired
	RetrievePartiesHandler retrievePartiesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdatePartyHandler updatePartyHandler;
	
	@Autowired
	UpdatePartiesHandler updatePartiesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeletePartyHandler deletePartyByIdHandler;
	
	@Autowired
	DeletePartiesHandler deletePartiesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createParty(ServerRequest request) {
		return this.createPartyHandler.createParty(request);
	}
	
	public Mono<ServerResponse> createParties(ServerRequest request) {
		return createPartiesHandler.createParties(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveParty(ServerRequest request) {
		return this.retrievePartyByIdHandler.retrieveParty(request);
	}
	
	public Mono<ServerResponse> retrieveParties(ServerRequest request) {
		return this.retrievePartiesHandler.retrieveParties(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateParty(ServerRequest request) {
		return this.updatePartyHandler.updateParty(request);
	}
	
	public Mono<ServerResponse> updateParties(ServerRequest request) {
		return this.updatePartiesHandler.updateParties(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteParty(ServerRequest request) {
		return this.deletePartyByIdHandler.deleteParty(request);
	}
	
	public Mono<ServerResponse> deleteParties(ServerRequest request) {
		return this.deletePartiesHandler.deleteParties(request);
	}	

	// </editor-fold>	

}
