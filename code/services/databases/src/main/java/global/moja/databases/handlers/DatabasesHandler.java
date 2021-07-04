/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers;

import global.moja.databases.handlers.delete.DeleteDatabaseHandler;
import global.moja.databases.handlers.delete.DeleteDatabasesHandler;
import global.moja.databases.handlers.get.RetrieveDatabaseHandler;
import global.moja.databases.handlers.get.RetrieveDatabaseTemporalScaleHandler;
import global.moja.databases.handlers.get.RetrieveDatabaseValidationResultsHandler;
import global.moja.databases.handlers.get.RetrieveDatabasesHandler;
import global.moja.databases.handlers.post.CreateDatabaseHandler;
import global.moja.databases.handlers.put.UpdateDatabaseHandler;
import global.moja.databases.handlers.put.UpdateDatabasesHandler;
import global.moja.databases.handlers.post.CreateDatabasesHandler;
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
public class DatabasesHandler {

	// POST HANDLERS
	@Autowired
    CreateDatabaseHandler createDatabaseHandler;

	@Autowired
	CreateDatabasesHandler createDatabasesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveDatabaseHandler retrieveDatabaseByIdHandler;
	
	@Autowired
    RetrieveDatabasesHandler retrieveDatabasesHandler;

	@Autowired
	RetrieveDatabaseValidationResultsHandler retrieveDatabaseValidationResultsHandler;

	@Autowired
	RetrieveDatabaseTemporalScaleHandler retrieveDatabaseTemporalScaleHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdateDatabaseHandler updateDatabaseHandler;
	
	@Autowired
    UpdateDatabasesHandler updateDatabasesHandler;

	
	// DELETE HANDLERS
	@Autowired
    DeleteDatabaseHandler deleteDatabaseByIdHandler;
	
	@Autowired
    DeleteDatabasesHandler deleteDatabasesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createDatabase(ServerRequest request) {
		return this.createDatabaseHandler.createDatabase(request);
	}
	
	public Mono<ServerResponse> createDatabases(ServerRequest request) {
		return createDatabasesHandler.createDatabases(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveDatabase(ServerRequest request) {
		return this.retrieveDatabaseByIdHandler.retrieveDatabase(request);
	}
	
	public Mono<ServerResponse> retrieveDatabases(ServerRequest request) {
		return this.retrieveDatabasesHandler.retrieveDatabases(request);
	}

	public Mono<ServerResponse> retrieveDatabaseValidationResults(ServerRequest request) {
		return this.retrieveDatabaseValidationResultsHandler.retrieveDatabaseValidationResults(request);
	}

	public Mono<ServerResponse> retrieveDatabaseTemporalScale(ServerRequest request) {
		return this.retrieveDatabaseTemporalScaleHandler.retrieveDatabaseTemporalScale(request);
	}

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateDatabase(ServerRequest request) {
		return this.updateDatabaseHandler.updateDatabase(request);
	}
	
	public Mono<ServerResponse> updateDatabases(ServerRequest request) {
		return this.updateDatabasesHandler.updateDatabases(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteDatabase(ServerRequest request) {
		return this.deleteDatabaseByIdHandler.deleteDatabase(request);
	}
	
	public Mono<ServerResponse> deleteDatabases(ServerRequest request) {
		return this.deleteDatabasesHandler.deleteDatabases(request);
	}	

	// </editor-fold>	

}
