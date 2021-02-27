/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers;

import global.moja.landusecategories.handlers.delete.DeleteLandUseCategoriesHandler;
import global.moja.landusecategories.handlers.delete.DeleteLandUseCategoryHandler;
import global.moja.landusecategories.handlers.get.RetrieveLandUseCategoriesHandler;
import global.moja.landusecategories.handlers.get.RetrieveLandUseCategoryHandler;
import global.moja.landusecategories.handlers.post.CreateLandUseCategoriesHandler;
import global.moja.landusecategories.handlers.post.CreateLandUseCategoryHandler;
import global.moja.landusecategories.handlers.put.UpdateLandUseCategoriesHandler;
import global.moja.landusecategories.handlers.put.UpdateLandUseCategoryHandler;
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
public class LandUseCategoriesHandler {

	// POST HANDLERS
	@Autowired
    CreateLandUseCategoryHandler createLandUseCategoryHandler;

	@Autowired
    CreateLandUseCategoriesHandler createLandUseCategoriesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveLandUseCategoryHandler retrieveLandUseCategoryByIdHandler;
	
	@Autowired
    RetrieveLandUseCategoriesHandler retrieveLandUseCategoriesHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdateLandUseCategoryHandler updateLandUseCategoryHandler;
	
	@Autowired
    UpdateLandUseCategoriesHandler updateLandUseCategoriesHandler;

	
	// DELETE HANDLERS
	@Autowired
    DeleteLandUseCategoryHandler deleteLandUseCategoryByIdHandler;
	
	@Autowired
    DeleteLandUseCategoriesHandler deleteLandUseCategoriesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createLandUseCategory(ServerRequest request) {
		return this.createLandUseCategoryHandler.createLandUseCategory(request);
	}
	
	public Mono<ServerResponse> createLandUseCategories(ServerRequest request) {
		return createLandUseCategoriesHandler.createLandUseCategories(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveLandUseCategory(ServerRequest request) {
		return this.retrieveLandUseCategoryByIdHandler.retrieveLandUseCategory(request);
	}
	
	public Mono<ServerResponse> retrieveLandUseCategories(ServerRequest request) {
		return this.retrieveLandUseCategoriesHandler.retrieveLandUseCategories(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateLandUseCategory(ServerRequest request) {
		return this.updateLandUseCategoryHandler.updateLandUseCategory(request);
	}
	
	public Mono<ServerResponse> updateLandUseCategories(ServerRequest request) {
		return this.updateLandUseCategoriesHandler.updateLandUseCategories(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteLandUseCategory(ServerRequest request) {
		return this.deleteLandUseCategoryByIdHandler.deleteLandUseCategory(request);
	}
	
	public Mono<ServerResponse> deleteLandUseCategories(ServerRequest request) {
		return this.deleteLandUseCategoriesHandler.deleteLandUseCategories(request);
	}	

	// </editor-fold>	

}
