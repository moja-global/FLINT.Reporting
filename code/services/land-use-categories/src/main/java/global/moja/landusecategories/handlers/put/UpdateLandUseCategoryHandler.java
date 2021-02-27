/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers.put;

import global.moja.landusecategories.exceptions.ServerException;
import global.moja.landusecategories.models.LandUseCategory;
import global.moja.landusecategories.repository.LandUseCategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UpdateLandUseCategoryHandler {

	@Autowired
    LandUseCategoriesRepository repository;
	
	/**
	 * Updates a Land Use Category record
	 * @param request the request containing the details of the Land Use Category record to be updated
	 * @return the response containing the details of the newly updated Land Use Category record
	 */
	public Mono<ServerResponse> updateLandUseCategory(ServerRequest request) {

		log.trace("Entering updateLandUseCategory()");

		return 
			request
				.bodyToMono(LandUseCategory.class)
					.flatMap(landUseCategory ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateLandUseCategory(landUseCategory), LandUseCategory.class))
					.onErrorMap(e -> new ServerException("Land Use Category update failed", e));
	}
	
	
	private Mono<LandUseCategory> updateLandUseCategory(LandUseCategory landUseCategory){
		
		return 
			repository
				.updateLandUseCategory(landUseCategory)
				.flatMap(count -> repository.selectLandUseCategory(landUseCategory.getId()));
	}
}
