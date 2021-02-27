/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers.get;

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
public class RetrieveLandUseCategoryHandler {

	@Autowired
    LandUseCategoriesRepository repository;

	/**
	 * Retrieves a Land Use Category record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Land Use Category record to be retrieved
	 * @return the response containing the details of the retrieved Land Use Category record
	 */
	public Mono<ServerResponse> retrieveLandUseCategory(ServerRequest request) {

		log.trace("Entering retrieveLandUseCategory()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveLandUseCategoryById(Long.parseLong(request.pathVariable("id"))),
                        LandUseCategory.class)
				.onErrorMap(e -> new ServerException("Land Use Category retrieval failed", e));
	}


	private Mono<LandUseCategory> retrieveLandUseCategoryById(Long id) {

		return
			repository
				.selectLandUseCategory(id);
	}

}
