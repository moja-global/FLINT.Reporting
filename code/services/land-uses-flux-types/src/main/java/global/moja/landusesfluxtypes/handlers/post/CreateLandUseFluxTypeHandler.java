/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.handlers.post;

import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.repository.LandUsesFluxTypesRepository;
import global.moja.landusesfluxtypes.exceptions.ServerException;
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
public class CreateLandUseFluxTypeHandler {

	@Autowired
    LandUsesFluxTypesRepository repository;
	
	/**
	 * Creates a Land Use Flux Type record
	 *
	 * @param request the request containing the details of the Land Use Flux Type record to be created
	 * @return the response containing the details of the newly created Land Use Flux Type record
	 */
	public Mono<ServerResponse> createLandUseFluxType(ServerRequest request) {

		log.trace("Entering createLandUseFluxType()");

		return 
			request
				.bodyToMono(LandUseFluxType.class)
				.flatMap(landUseFluxType ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createLandUseFluxType(landUseFluxType), LandUseFluxType.class))
				.onErrorMap(e -> new ServerException("Land Use Flux Type creation failed", e));
	}
	
	
	private Mono<LandUseFluxType> createLandUseFluxType(LandUseFluxType landUseFluxType){
		
		return 
			repository
				.insertLandUseFluxType(landUseFluxType)
				.flatMap(id -> repository.selectLandUseFluxType(id));
		  
	}

}
