/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers.post;

import global.moja.landusecategories.exceptions.ServerException;
import global.moja.landusecategories.models.LandUseCategory;
import global.moja.landusecategories.repository.LandUseCategoriesRepository;
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
public class CreateLandUseCategoryHandler {

	@Autowired
    LandUseCategoriesRepository repository;
	
	/**
	 * Creates a Land Use Category record
	 *
	 * @param request the request containing the details of the Land Use Category record to be created
	 * @return the response containing the details of the newly created Land Use Category record
	 */
	public Mono<ServerResponse> createLandUseCategory(ServerRequest request) {

		log.trace("Entering createLandUseCategory()");

		return 
			request
				.bodyToMono(LandUseCategory.class)
				.flatMap(landUseCategory ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createLandUseCategory(landUseCategory), LandUseCategory.class))
				.onErrorMap(e -> new ServerException("Land Use Category creation failed", e));
	}
	
	
	private Mono<LandUseCategory> createLandUseCategory(LandUseCategory landUseCategory){
		
		return 
			repository
				.insertLandUseCategory(landUseCategory)
				.flatMap(id -> repository.selectLandUseCategory(id));
		  
	}

}
