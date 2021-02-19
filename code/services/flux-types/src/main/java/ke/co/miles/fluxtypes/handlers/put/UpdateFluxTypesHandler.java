/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.handlers.put;

import ke.co.miles.fluxtypes.models.FluxType;
import ke.co.miles.fluxtypes.exceptions.ServerException;
import ke.co.miles.fluxtypes.repository.FluxTypesRepository;
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
public class UpdateFluxTypesHandler {

	@Autowired
	FluxTypesRepository repository;
	
	/**
	 * Updates flux type records
	 * @param request the request containing the details of the flux type records to be updated
	 * @return the response containing the details of the newly updated flux type records
	 */
	public Mono<ServerResponse> updateFluxTypes(ServerRequest request) {

		log.trace("Entering updateFluxTypes()");

		return 
			request
					.bodyToMono(FluxType[].class)
					.flatMap(units ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateFluxTypes(units), FluxType.class))
					.onErrorMap(e -> new ServerException("Flux Type records update failed", e));
	}
	
	private Flux<FluxType> updateFluxTypes(FluxType[] fluxTypes) {
		return 
			Flux.fromStream(Arrays.stream(fluxTypes).sorted())
				.flatMap(unit -> 
					repository
						.updateFluxType(unit)
						.flatMap(count -> repository.selectFluxTypeById(unit.getId())));

	}

	


}
