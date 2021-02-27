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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class RetrieveUnitCategoriesHandler {

	@Autowired
	UnitCategoriesRepository repository;
		
	/**
	 * Retrieves all unit category records or specific unit category records if given their unique identifiers
	 * @param request the request, optionally containing the unique identifiers of the unit category records to be retrieved
	 * @return the stream of responses containing the details of the retrieved unit category records
	 */
	public Mono<ServerResponse> retrieveUnitCategories(ServerRequest request) {

		log.trace("Entering retrieveUnitCategories()");

		return
			Mono.just(getIds(request))
				.flatMap(ids ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(ids.length == 0 ? retrieveAllUnitCategories() : retrieveUnitCategoriesByIds(ids), UnitCategory.class))
				.onErrorMap(e -> new ServerException("Unit Category records retrieval failed", e));
	}
	
	private Flux<UnitCategory> retrieveAllUnitCategories() {
		return 
			repository
				.selectAllUnitCategories();
	}		

	private Flux<UnitCategory> retrieveUnitCategoriesByIds(Long[] ids) {
		
		return 
			repository
				.selectUnitCategoriesByIds(ids);
	}

	private Long[] getIds(ServerRequest request) {

		return
				request.queryParams().get("ids") == null ? new Long[]{}:
						request.queryParams()
								.get("ids")
								.stream()
								.map(Long::parseLong)
								.sorted()
								.toArray(Long[]::new);

	}

}
