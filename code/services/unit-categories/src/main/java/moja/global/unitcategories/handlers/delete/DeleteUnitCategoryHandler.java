/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.handlers.delete;

import moja.global.unitcategories.exceptions.ServerException;
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
public class DeleteUnitCategoryHandler {

	@Autowired
	UnitCategoriesRepository repository;
	
	/**
	 * Deletes a unit category record
	 * @param request the request containing the details of the unit category record to be deleted
	 * @return the response containing the number of records deleted
	 */
	public Mono<ServerResponse> deleteUnitCategory(ServerRequest request) {

		log.trace("Entering deleteUnitCategory()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteUnitCategoryById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Unit Category record deletion failed", e));

	}
	
	private Mono<Integer> deleteUnitCategoryById(Long id){
		
		return 
			repository
				.deleteUnitCategoryById(id);
	}

}
