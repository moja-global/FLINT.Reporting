/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.handlers.get;

import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.exceptions.ServerException;
import global.moja.emissiontypes.repository.EmissionTypesRepository;
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
public class RetrieveEmissionTypeHandler {

	@Autowired
    EmissionTypesRepository repository;

	/**
	 * Retrieves a Emission Type record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Emission Type record to be retrieved
	 * @return the response containing the details of the retrieved Emission Type record
	 */
	public Mono<ServerResponse> retrieveEmissionType(ServerRequest request) {

		log.trace("Entering retrieveEmissionType()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveEmissionTypeById(Long.parseLong(request.pathVariable("id"))),
                        EmissionType.class)
				.onErrorMap(e -> new ServerException("Emission Type retrieval failed", e));
	}


	private Mono<EmissionType> retrieveEmissionTypeById(Long id) {

		return
			repository
				.selectEmissionType(id);
	}

}
