/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.handlers.put;

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
public class UpdateEmissionTypeHandler {

	@Autowired
    EmissionTypesRepository repository;
	
	/**
	 * Updates a Emission Type record
	 * @param request the request containing the details of the Emission Type record to be updated
	 * @return the response containing the details of the newly updated Emission Type record
	 */
	public Mono<ServerResponse> updateEmissionType(ServerRequest request) {

		log.trace("Entering updateEmissionType()");

		return 
			request
				.bodyToMono(EmissionType.class)
					.flatMap(emissionType ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateEmissionType(emissionType), EmissionType.class))
					.onErrorMap(e -> new ServerException("Emission Type update failed", e));
	}
	
	
	private Mono<EmissionType> updateEmissionType(EmissionType emissionType){
		
		return 
			repository
				.updateEmissionType(emissionType)
				.flatMap(count -> repository.selectEmissionType(emissionType.getId()));
	}
}
