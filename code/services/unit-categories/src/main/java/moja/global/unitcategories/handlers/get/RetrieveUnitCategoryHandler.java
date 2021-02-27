/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.handlers.get;

import moja.global.unitcategories.exceptions.ServerException;
import moja.global.unitcategories.models.UnitCategory;
import moja.global.unitcategories.repository.UnitCategoriesRepository;
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
public class RetrieveUnitCategoryHandler {

	@Autowired
	UnitCategoriesRepository repository;
	
	/**
	 * Retrieves a unit category record given its unique identifier
	 * @param request the request containing the unique identifier of the unit category record to be retrieved
	 * @return the response containing the details of the retrieved unit category record
	 */
	public Mono<ServerResponse> retrieveUnitCategory(ServerRequest request) {

		log.trace("Entering retrieveUnitCategoryById()");

		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveUnitCategoryById(Long.parseLong(request.pathVariable("id"))), UnitCategory.class)
				.onErrorMap(e -> new ServerException("Unit Category record retrieval failed", e));
	}
	
	
	private Mono<UnitCategory> retrieveUnitCategoryById(Long id) {
		
		return 
			repository
				.selectUnitCategoryById(id);
	}

}
