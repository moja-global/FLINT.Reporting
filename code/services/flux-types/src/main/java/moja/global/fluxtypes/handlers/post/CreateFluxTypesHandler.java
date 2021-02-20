/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes.handlers.post;

import moja.global.fluxtypes.exceptions.ServerException;
import moja.global.fluxtypes.models.FluxType;
import moja.global.fluxtypes.repository.FluxTypesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class CreateFluxTypesHandler {

	@Autowired
	FluxTypesRepository repository;
	

	/**
	 * Recursively creates flux types records
	 * @param request the request containing the details of the flux type records to be created
	 * @return the stream of responses containing the details of the newly created flux type records
	 */
	public Mono<ServerResponse> createFluxTypes(ServerRequest request) {

		log.trace("Entering createFluxTypes()");

		return 
			request
				.bodyToMono(FluxType[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createFluxTypes(units), FluxType.class))
				.onErrorMap(e -> new ServerException("Flux Type records creation failed", e));
	}
	
	private Flux<FluxType> createFluxTypes(FluxType[] fluxTypes) {
		
		return
			repository
				.insertFluxTypes(fluxTypes)
				.flatMap(id -> repository.selectFluxTypeById(id));
			
	}



}
