/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.handlers.put;

import ke.co.miles.fluxtypes.exceptions.ServerException;
import ke.co.miles.fluxtypes.models.FluxType;
import ke.co.miles.fluxtypes.repository.FluxTypesRepository;
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
public class UpdateFluxTypeHandler {

	@Autowired
	FluxTypesRepository repository;
	
	/**
	 * Updates a flux type record
	 * @param request the request containing the details of the flux type record to be updated
	 * @return the response containing the details of the newly updated flux type record
	 */
	public Mono<ServerResponse> updateFluxType(ServerRequest request) {

		log.trace("Entering updateFluxType()");

		return 
			request
				.bodyToMono(FluxType.class)
					.flatMap(unit -> 
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateFluxType(unit), FluxType.class))
					.onErrorMap(e -> new ServerException("Flux Type record update failed", e));
	}
	
	
	private Mono<FluxType> updateFluxType(FluxType fluxType){
		
		return 
			repository
				.updateFluxType(fluxType)
				.flatMap(count -> repository.selectFluxTypeById(fluxType.getId()));
	}
}
