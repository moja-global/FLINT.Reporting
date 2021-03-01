/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.handlers;

import global.moja.landusesfluxtypes.handlers.get.RetrieveLandUseFluxTypeHandler;
import global.moja.landusesfluxtypes.handlers.delete.DeleteLandUseFluxTypeHandler;
import global.moja.landusesfluxtypes.handlers.delete.DeleteLandUsesFluxTypesHandler;
import global.moja.landusesfluxtypes.handlers.get.RetrieveLandUsesFluxTypesHandler;
import global.moja.landusesfluxtypes.handlers.put.UpdateLandUseFluxTypeHandler;
import global.moja.landusesfluxtypes.handlers.put.UpdateLandUsesFluxTypesHandler;
import global.moja.landusesfluxtypes.handlers.post.CreateLandUsesFluxTypesHandler;
import global.moja.landusesfluxtypes.handlers.post.CreateLandUseFluxTypeHandler;
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
public class LandUsesFluxTypesHandler {

	// POST HANDLERS
	@Autowired
	CreateLandUseFluxTypeHandler createLandUseFluxTypeHandler;

	@Autowired
	CreateLandUsesFluxTypesHandler createLandUsesFluxTypesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveLandUseFluxTypeHandler retrieveLandUseFluxTypeByIdHandler;
	
	@Autowired
	RetrieveLandUsesFluxTypesHandler retrieveLandUsesFluxTypesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateLandUseFluxTypeHandler updateLandUseFluxTypeHandler;
	
	@Autowired
	UpdateLandUsesFluxTypesHandler updateLandUsesFluxTypesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteLandUseFluxTypeHandler deleteLandUseFluxTypeByIdHandler;
	
	@Autowired
	DeleteLandUsesFluxTypesHandler deleteLandUsesFluxTypesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createLandUseFluxType(ServerRequest request) {
		return this.createLandUseFluxTypeHandler.createLandUseFluxType(request);
	}
	
	public Mono<ServerResponse> createLandUsesFluxTypes(ServerRequest request) {
		return createLandUsesFluxTypesHandler.createLandUsesFluxTypes(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveLandUseFluxType(ServerRequest request) {
		return this.retrieveLandUseFluxTypeByIdHandler.retrieveLandUseFluxType(request);
	}
	
	public Mono<ServerResponse> retrieveLandUsesFluxTypes(ServerRequest request) {
		return this.retrieveLandUsesFluxTypesHandler.retrieveLandUsesFluxTypes(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateLandUseFluxType(ServerRequest request) {
		return this.updateLandUseFluxTypeHandler.updateLandUseFluxType(request);
	}
	
	public Mono<ServerResponse> updateLandUsesFluxTypes(ServerRequest request) {
		return this.updateLandUsesFluxTypesHandler.updateLandUsesFluxTypes(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteLandUseFluxType(ServerRequest request) {
		return this.deleteLandUseFluxTypeByIdHandler.deleteLandUseFluxType(request);
	}
	
	public Mono<ServerResponse> deleteLandUsesFluxTypes(ServerRequest request) {
		return this.deleteLandUsesFluxTypesHandler.deleteLandUsesFluxTypes(request);
	}	

	// </editor-fold>	

}
