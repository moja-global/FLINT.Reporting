/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.handlers.get;

import global.moja.vegetationtypes.exceptions.ServerException;
import global.moja.vegetationtypes.models.VegetationType;
import global.moja.vegetationtypes.repository.VegetationTypesRepository;
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
public class RetrieveVegetationTypeHandler {

	@Autowired
    VegetationTypesRepository repository;

	/**
	 * Retrieves a Vegetation Type record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Vegetation Type record to be retrieved
	 * @return the response containing the details of the retrieved Vegetation Type record
	 */
	public Mono<ServerResponse> retrieveVegetationType(ServerRequest request) {

		log.trace("Entering retrieveVegetationType()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveVegetationTypeById(request),
                        VegetationType.class)
				.onErrorMap(e -> new ServerException("Vegetation Type retrieval failed", e));
	}


	private Mono<VegetationType> retrieveVegetationTypeById(ServerRequest request) {

		return
			repository
				.selectVegetationType(
						Long.parseLong(request.pathVariable("databaseId")),
						Long.parseLong(request.pathVariable("id")));
	}

}
