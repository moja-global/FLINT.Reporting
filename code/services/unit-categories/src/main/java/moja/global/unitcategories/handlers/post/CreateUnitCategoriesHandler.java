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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateUnitCategoriesHandler {

	@Autowired
	UnitCategoriesRepository repository;
	

	/**
	 * Recursively creates unit categories records
	 * @param request the request containing the details of the unit category records to be created
	 * @return the stream of responses containing the details of the newly created unit category records
	 */
	public Mono<ServerResponse> createUnitCategories(ServerRequest request) {

		log.trace("Entering createUnitCategories()");

		return 
			request
				.bodyToMono(UnitCategory[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createUnitCategories(units), UnitCategory.class))
				.onErrorMap(e -> new ServerException("Unit Category records creation failed", e));
	}
	
	private Flux<UnitCategory> createUnitCategories(UnitCategory[] unitCategories) {
		
		return
			repository
				.insertUnitCategories(unitCategories)
				.flatMap(id -> repository.selectUnitCategoryById(id));
			
	}



}
