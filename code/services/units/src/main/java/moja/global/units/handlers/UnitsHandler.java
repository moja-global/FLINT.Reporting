/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.units.handlers;

import moja.global.units.handlers.delete.DeleteUnitHandler;
import moja.global.units.handlers.delete.DeleteUnitsHandler;
import moja.global.units.handlers.get.RetrieveUnitHandler;
import moja.global.units.handlers.get.RetrieveUnitsHandler;
import moja.global.units.handlers.post.CreateUnitHandler;
import moja.global.units.handlers.post.CreateUnitsHandler;
import moja.global.units.handlers.put.UpdateUnitHandler;
import moja.global.units.handlers.put.UpdateUnitsHandler;
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
public class UnitsHandler {

	// POST HANDLERS
	@Autowired
    CreateUnitHandler createUnitHandler;

	@Autowired
    CreateUnitsHandler createUnitsHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveUnitHandler retrieveUnitByIdHandler;
	
	@Autowired
    RetrieveUnitsHandler retrieveUnitsHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdateUnitHandler updateUnitHandler;
	
	@Autowired
    UpdateUnitsHandler updateUnitsHandler;

	
	// DELETE HANDLERS
	@Autowired
    DeleteUnitHandler deleteUnitByIdHandler;
	
	@Autowired
    DeleteUnitsHandler deleteUnitsHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createUnit(ServerRequest request) {
		return this.createUnitHandler.createUnit(request);
	}
	
	public Mono<ServerResponse> createUnits(ServerRequest request) {
		return createUnitsHandler.createUnits(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveUnit(ServerRequest request) {
		return this.retrieveUnitByIdHandler.retrieveUnit(request);
	}
	
	public Mono<ServerResponse> retrieveUnits(ServerRequest request) {
		return this.retrieveUnitsHandler.retrieveUnits(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateUnit(ServerRequest request) {
		return this.updateUnitHandler.updateUnit(request);
	}
	
	public Mono<ServerResponse> updateUnits(ServerRequest request) {
		return this.updateUnitsHandler.updateUnits(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteUnit(ServerRequest request) {
		return this.deleteUnitByIdHandler.deleteUnit(request);
	}
	
	public Mono<ServerResponse> deleteUnits(ServerRequest request) {
		return this.deleteUnitsHandler.deleteUnits(request);
	}	

	// </editor-fold>	

}
