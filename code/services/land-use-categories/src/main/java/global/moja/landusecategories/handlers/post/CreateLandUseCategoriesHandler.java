/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers.post;

import global.moja.landusecategories.repository.LandUseCategoriesRepository;
import global.moja.landusecategories.exceptions.ServerException;
import global.moja.landusecategories.models.LandUseCategory;
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
public class CreateLandUseCategoriesHandler {

	@Autowired
    LandUseCategoriesRepository repository;
	

	/**
	 * Recursively creates Land Use Categories records
	 *
	 * @param request the request containing the details of the Land Use Categories records to be created
	 * @return the stream of responses containing the details of the newly created Land Use Categories records
	 */
	public Mono<ServerResponse> createLandUseCategories(ServerRequest request) {

		log.trace("Entering createLandUseCategories()");

		return 
			request
				.bodyToMono(LandUseCategory[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createLandUseCategories(units), LandUseCategory.class))
				.onErrorMap(e -> new ServerException("Land Use Categories creation failed", e));
	}
	
	private Flux<LandUseCategory> createLandUseCategories(LandUseCategory[] landUseCategories) {
		
		return
			repository
				.insertLandUseCategories(landUseCategories)
				.flatMap(id -> repository.selectLandUseCategory(id));
			
	}



}
