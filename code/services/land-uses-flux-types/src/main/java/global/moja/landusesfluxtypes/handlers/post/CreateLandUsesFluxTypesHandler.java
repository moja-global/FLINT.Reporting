/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.handlers.post;

import global.moja.landusesfluxtypes.exceptions.ServerException;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.repository.LandUsesFluxTypesRepository;
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
public class CreateLandUsesFluxTypesHandler {

	@Autowired
    LandUsesFluxTypesRepository repository;
	

	/**
	 * Recursively creates Land Uses Flux Types records
	 *
	 * @param request the request containing the details of the Land Uses Flux Types records to be created
	 * @return the stream of responses containing the details of the newly created Land Uses Flux Types records
	 */
	public Mono<ServerResponse> createLandUsesFluxTypes(ServerRequest request) {

		log.trace("Entering createLandUsesFluxTypes()");

		return 
			request
				.bodyToMono(LandUseFluxType[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createLandUsesFluxTypes(units), LandUseFluxType.class))
				.onErrorMap(e -> new ServerException("Land Uses Flux Types creation failed", e));
	}
	
	private Flux<LandUseFluxType> createLandUsesFluxTypes(LandUseFluxType[] landUsesFluxTypes) {
		
		return
			repository
				.insertLandUsesFluxTypes(landUsesFluxTypes)
				.flatMap(id -> repository.selectLandUseFluxType(id));
			
	}



}
