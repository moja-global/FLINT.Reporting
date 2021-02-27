/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.handlers.post;

import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.exceptions.ServerException;
import global.moja.emissiontypes.repository.EmissionTypesRepository;
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
public class CreateEmissionTypesHandler {

	@Autowired
    EmissionTypesRepository repository;
	

	/**
	 * Recursively creates Emission Types records
	 *
	 * @param request the request containing the details of the Emission Types records to be created
	 * @return the stream of responses containing the details of the newly created Emission Types records
	 */
	public Mono<ServerResponse> createEmissionTypes(ServerRequest request) {

		log.trace("Entering createEmissionTypes()");

		return 
			request
				.bodyToMono(EmissionType[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createEmissionTypes(units), EmissionType.class))
				.onErrorMap(e -> new ServerException("Emission Types creation failed", e));
	}
	
	private Flux<EmissionType> createEmissionTypes(EmissionType[] emissionTypes) {
		
		return
			repository
				.insertEmissionTypes(emissionTypes)
				.flatMap(id -> repository.selectEmissionType(id));
			
	}



}
