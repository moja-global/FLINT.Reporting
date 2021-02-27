/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.handlers.get;

import global.moja.fluxtypes.models.FluxType;
import global.moja.fluxtypes.exceptions.ServerException;
import global.moja.fluxtypes.repository.FluxTypesRepository;
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
public class RetrieveFluxTypeHandler {

	@Autowired
    FluxTypesRepository repository;

	/**
	 * Retrieves a Flux Type record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Flux Type record to be retrieved
	 * @return the response containing the details of the retrieved Flux Type record
	 */
	public Mono<ServerResponse> retrieveFluxType(ServerRequest request) {

		log.trace("Entering retrieveFluxType()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveFluxTypeById(Long.parseLong(request.pathVariable("id"))),
                        FluxType.class)
				.onErrorMap(e -> new ServerException("Flux Type retrieval failed", e));
	}


	private Mono<FluxType> retrieveFluxTypeById(Long id) {

		return
			repository
				.selectFluxType(id);
	}

}
