/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.handlers.put;

import moja.global.unitcategories.models.UnitCategory;
import moja.global.unitcategories.exceptions.ServerException;
import moja.global.unitcategories.repository.UnitCategoriesRepository;
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
public class UpdateUnitCategoriesHandler {

	@Autowired
	UnitCategoriesRepository repository;
	
	/**
	 * Updates unit category records
	 * @param request the request containing the details of the unit category records to be updated
	 * @return the response containing the details of the newly updated unit category records
	 */
	public Mono<ServerResponse> updateUnitCategories(ServerRequest request) {

		log.trace("Entering updateUnitCategories()");

		return 
			request
					.bodyToMono(UnitCategory[].class)
					.flatMap(units ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateUnitCategories(units), UnitCategory.class))
					.onErrorMap(e -> new ServerException("Unit Category records update failed", e));
	}
	
	private Flux<UnitCategory> updateUnitCategories(UnitCategory[] unitCategories) {
		return 
			Flux.fromStream(Arrays.stream(unitCategories).sorted())
				.flatMap(unit -> 
					repository
						.updateUnitCategory(unit)
						.flatMap(count -> repository.selectUnitCategoryById(unit.getId())));

	}

	


}
