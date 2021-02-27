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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateEmissionTypesHandler {

	@Autowired
    EmissionTypesRepository repository;
	
	/**
	 * Updates Emission Types records
	 * @param request the request containing the details of the Emission Types records to be updated
	 * @return the response containing the details of the newly updated Emission Types records
	 */
	public Mono<ServerResponse> updateEmissionTypes(ServerRequest request) {

		log.trace("Entering updateEmissionTypes()");

		return 
			request
					.bodyToMono(EmissionType[].class)
					.flatMap(emissionTypes->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateEmissionTypes(emissionTypes), EmissionType.class))
					.onErrorMap(e -> new ServerException("Emission Types update failed", e));
	}
	
	private Flux<EmissionType> updateEmissionTypes(EmissionType[] emissionTypes) {
		return 
			Flux.fromStream(Arrays.stream(emissionTypes).sorted())
				.flatMap(unit -> 
					repository
						.updateEmissionType(unit)
						.flatMap(count -> repository.selectEmissionType(unit.getId())));

	}

	


}
