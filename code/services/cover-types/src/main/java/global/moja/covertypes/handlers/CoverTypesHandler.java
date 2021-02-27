/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.handlers;

import global.moja.covertypes.handlers.delete.DeleteCoverTypeHandler;
import global.moja.covertypes.handlers.delete.DeleteCoverTypesHandler;
import global.moja.covertypes.handlers.get.RetrieveCoverTypesHandler;
import global.moja.covertypes.handlers.put.UpdateCoverTypeHandler;
import global.moja.covertypes.handlers.put.UpdateCoverTypesHandler;
import global.moja.covertypes.handlers.post.CreateCoverTypesHandler;
import global.moja.covertypes.handlers.get.RetrieveCoverTypeHandler;
import global.moja.covertypes.handlers.post.CreateCoverTypeHandler;
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
public class CoverTypesHandler {

	// POST HANDLERS
	@Autowired
	CreateCoverTypeHandler createCoverTypeHandler;

	@Autowired
	CreateCoverTypesHandler createCoverTypesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveCoverTypeHandler retrieveCoverTypeByIdHandler;
	
	@Autowired
	RetrieveCoverTypesHandler retrieveCoverTypesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateCoverTypeHandler updateCoverTypeHandler;
	
	@Autowired
	UpdateCoverTypesHandler updateCoverTypesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteCoverTypeHandler deleteCoverTypeByIdHandler;
	
	@Autowired
	DeleteCoverTypesHandler deleteCoverTypesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createCoverType(ServerRequest request) {
		return this.createCoverTypeHandler.createCoverType(request);
	}
	
	public Mono<ServerResponse> createCoverTypes(ServerRequest request) {
		return createCoverTypesHandler.createCoverTypes(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveCoverType(ServerRequest request) {
		return this.retrieveCoverTypeByIdHandler.retrieveCoverType(request);
	}
	
	public Mono<ServerResponse> retrieveCoverTypes(ServerRequest request) {
		return this.retrieveCoverTypesHandler.retrieveCoverTypes(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateCoverType(ServerRequest request) {
		return this.updateCoverTypeHandler.updateCoverType(request);
	}
	
	public Mono<ServerResponse> updateCoverTypes(ServerRequest request) {
		return this.updateCoverTypesHandler.updateCoverTypes(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteCoverType(ServerRequest request) {
		return this.deleteCoverTypeByIdHandler.deleteCoverType(request);
	}
	
	public Mono<ServerResponse> deleteCoverTypes(ServerRequest request) {
		return this.deleteCoverTypesHandler.deleteCoverTypes(request);
	}	

	// </editor-fold>	

}
