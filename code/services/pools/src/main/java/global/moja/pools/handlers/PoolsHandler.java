/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers;

import global.moja.pools.handlers.post.CreatePoolsHandler;
import global.moja.pools.handlers.delete.DeletePoolHandler;
import global.moja.pools.handlers.delete.DeletePoolsHandler;
import global.moja.pools.handlers.get.RetrievePoolsHandler;
import global.moja.pools.handlers.put.UpdatePoolHandler;
import global.moja.pools.handlers.put.UpdatePoolsHandler;
import global.moja.pools.handlers.get.RetrievePoolHandler;
import global.moja.pools.handlers.post.CreatePoolHandler;
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
public class PoolsHandler {

	// POST HANDLERS
	@Autowired
	CreatePoolHandler createPoolHandler;

	@Autowired
    CreatePoolsHandler createPoolsHandler;

	
	// GET HANDLERS
	@Autowired
	RetrievePoolHandler retrievePoolByIdHandler;
	
	@Autowired
	RetrievePoolsHandler retrievePoolsHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdatePoolHandler updatePoolHandler;
	
	@Autowired
	UpdatePoolsHandler updatePoolsHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeletePoolHandler deletePoolByIdHandler;
	
	@Autowired
	DeletePoolsHandler deletePoolsHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createPool(ServerRequest request) {
		return this.createPoolHandler.createPool(request);
	}
	
	public Mono<ServerResponse> createPools(ServerRequest request) {
		return createPoolsHandler.createPools(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrievePool(ServerRequest request) {
		return this.retrievePoolByIdHandler.retrievePool(request);
	}
	
	public Mono<ServerResponse> retrievePools(ServerRequest request) {
		return this.retrievePoolsHandler.retrievePools(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updatePool(ServerRequest request) {
		return this.updatePoolHandler.updatePool(request);
	}
	
	public Mono<ServerResponse> updatePools(ServerRequest request) {
		return this.updatePoolsHandler.updatePools(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deletePool(ServerRequest request) {
		return this.deletePoolByIdHandler.deletePool(request);
	}
	
	public Mono<ServerResponse> deletePools(ServerRequest request) {
		return this.deletePoolsHandler.deletePools(request);
	}	

	// </editor-fold>	

}
