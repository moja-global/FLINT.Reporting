/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.handlers.post;

import global.moja.fluxtypes.models.FluxType;
import global.moja.fluxtypes.exceptions.ServerException;
import global.moja.fluxtypes.repository.FluxTypesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class CreateFluxTypeHandler {

	@Autowired
    FluxTypesRepository repository;
	
	/**
	 * Creates a Flux Type record
	 *
	 * @param request the request containing the details of the Flux Type record to be created
	 * @return the response containing the details of the newly created Flux Type record
	 */
	public Mono<ServerResponse> createFluxType(ServerRequest request) {

		log.trace("Entering createFluxType()");

		return 
			request
				.bodyToMono(FluxType.class)
				.flatMap(fluxType ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createFluxType(fluxType), FluxType.class))
				.onErrorMap(e -> new ServerException("Flux Type creation failed", e));
	}
	
	
	private Mono<FluxType> createFluxType(FluxType fluxType){
		
		return 
			repository
				.insertFluxType(fluxType)
				.flatMap(id -> repository.selectFluxType(id));
		  
	}

}
