/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.handlers;

import moja.global.unitcategories.handlers.post.CreateUnitCategoriesHandler;
import moja.global.unitcategories.handlers.delete.DeleteUnitCategoryHandler;
import moja.global.unitcategories.handlers.delete.DeleteUnitCategoriesHandler;
import moja.global.unitcategories.handlers.get.RetrieveUnitCategoryHandler;
import moja.global.unitcategories.handlers.get.RetrieveUnitCategoriesHandler;
import moja.global.unitcategories.handlers.post.CreateUnitCategoryHandler;
import moja.global.unitcategories.handlers.put.UpdateUnitCategoryHandler;
import moja.global.unitcategories.handlers.put.UpdateUnitCategoriesHandler;
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
public class UnitCategoriesHandler {

	// POST HANDLERS
	@Autowired
	CreateUnitCategoryHandler createUnitCategoryHandler;

	@Autowired
	CreateUnitCategoriesHandler createUnitCategoriesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveUnitCategoryHandler retrieveUnitCategoryByIdHandler;
	
	@Autowired
	RetrieveUnitCategoriesHandler retrieveUnitCategoriesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateUnitCategoryHandler updateUnitCategoryHandler;
	
	@Autowired
	UpdateUnitCategoriesHandler updateUnitCategoriesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteUnitCategoryHandler deleteUnitCategoryByIdHandler;
	
	@Autowired
	DeleteUnitCategoriesHandler deleteUnitCategoriesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createUnitCategory(ServerRequest request) {
		return this.createUnitCategoryHandler.createUnitCategory(request);
	}
	
	public Mono<ServerResponse> createUnitCategories(ServerRequest request) {
		return createUnitCategoriesHandler.createUnitCategories(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveUnitCategory(ServerRequest request) {
		return this.retrieveUnitCategoryByIdHandler.retrieveUnitCategory(request);
	}
	
	public Mono<ServerResponse> retrieveUnitCategories(ServerRequest request) {
		return this.retrieveUnitCategoriesHandler.retrieveUnitCategories(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateUnitCategory(ServerRequest request) {
		return this.updateUnitCategoryHandler.updateUnitCategory(request);
	}
	
	public Mono<ServerResponse> updateUnitCategories(ServerRequest request) {
		return this.updateUnitCategoriesHandler.updateUnitCategories(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteUnitCategory(ServerRequest request) {
		return this.deleteUnitCategoryByIdHandler.deleteUnitCategory(request);
	}
	
	public Mono<ServerResponse> deleteUnitCategories(ServerRequest request) {
		return this.deleteUnitCategoriesHandler.deleteUnitCategories(request);
	}	

	// </editor-fold>	

}
