/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.handlers.post;

import moja.global.emissiontypes.exceptions.ServerException;
import moja.global.emissiontypes.models.EmissionType;
import moja.global.emissiontypes.repository.EmissionTypesRepository;
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
public class CreateEmissionTypeHandler {

	@Autowired
	EmissionTypesRepository repository;
	
	/**
	 * Creates an emission type record
	 * @param request the request containing the details of the emission type record to be created
	 * @return the response containing the details of the newly created emission type record
	 */
	public Mono<ServerResponse> createEmissionType(ServerRequest request) {

		log.trace("Entering createEmissionType()");

		return 
			request
				.bodyToMono(EmissionType.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createEmissionType(unit), EmissionType.class))
				.onErrorMap(e -> new ServerException("Emission Type record creation failed", e));
	}
	
	
	private Mono<EmissionType> createEmissionType(EmissionType emissionType){
		
		return 
			repository
				.insertEmissionType(emissionType)
				.flatMap(id -> repository.selectEmissionTypeById(id));
		  
	}

}
