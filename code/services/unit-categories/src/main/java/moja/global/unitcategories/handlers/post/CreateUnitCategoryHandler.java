/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.handlers.post;

import moja.global.unitcategories.exceptions.ServerException;
import moja.global.unitcategories.models.UnitCategory;
import moja.global.unitcategories.repository.UnitCategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class CreateUnitCategoryHandler {

	@Autowired
	UnitCategoriesRepository repository;
	
	/**
	 * Creates a unit category record
	 * @param request the request containing the details of the unit category record to be created
	 * @return the response containing the details of the newly created unit category record
	 */
	public Mono<ServerResponse> createUnitCategory(ServerRequest request) {

		log.trace("Entering createUnitCategory()");

		return 
			request
				.bodyToMono(UnitCategory.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createUnitCategory(unit), UnitCategory.class))
				.onErrorMap(e -> new ServerException("Unit Category record creation failed", e));
	}
	
	
	private Mono<UnitCategory> createUnitCategory(UnitCategory unitCategory){
		
		return 
			repository
				.insertUnitCategory(unitCategory)
				.flatMap(id -> repository.selectUnitCategoryById(id));
		  
	}

}
