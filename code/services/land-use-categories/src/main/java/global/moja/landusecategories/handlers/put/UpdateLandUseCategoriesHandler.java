/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers.put;

import global.moja.landusecategories.repository.LandUseCategoriesRepository;
import global.moja.landusecategories.exceptions.ServerException;
import global.moja.landusecategories.models.LandUseCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateLandUseCategoriesHandler {

	@Autowired
    LandUseCategoriesRepository repository;
	
	/**
	 * Updates Land Use Categories records
	 * @param request the request containing the details of the Land Use Categories records to be updated
	 * @return the response containing the details of the newly updated Land Use Categories records
	 */
	public Mono<ServerResponse> updateLandUseCategories(ServerRequest request) {

		log.trace("Entering updateLandUseCategories()");

		return 
			request
					.bodyToMono(LandUseCategory[].class)
					.flatMap(landUseCategories->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateLandUseCategories(landUseCategories), LandUseCategory.class))
					.onErrorMap(e -> new ServerException("Land Use Categories update failed", e));
	}
	
	private Flux<LandUseCategory> updateLandUseCategories(LandUseCategory[] landUseCategories) {
		return 
			Flux.fromStream(Arrays.stream(landUseCategories).sorted())
				.flatMap(unit -> 
					repository
						.updateLandUseCategory(unit)
						.flatMap(count -> repository.selectLandUseCategory(unit.getId())));

	}

	


}
