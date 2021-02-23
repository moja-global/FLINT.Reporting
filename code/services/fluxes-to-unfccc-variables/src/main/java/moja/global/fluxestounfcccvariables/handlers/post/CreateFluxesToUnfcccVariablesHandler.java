/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.handlers.post;

import moja.global.fluxestounfcccvariables.exceptions.ServerException;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.repository.FluxesToUnfcccVariablesRepository;
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
public class CreateFluxesToUnfcccVariablesHandler {

	@Autowired
	FluxesToUnfcccVariablesRepository repository;
	

	/**
	 * Recursively creates fluxes to UNFCCC variables records
	 * @param request the request containing the details of the fluxes to UNFCCC variable records to be created
	 * @return the stream of responses containing the details of the newly created fluxes to UNFCCC variable records
	 */
	public Mono<ServerResponse> createFluxesToUnfcccVariables(ServerRequest request) {

		log.trace("Entering createFluxesToUnfcccVariables()");

		return 
			request
				.bodyToMono(FluxToUnfcccVariable[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createFluxesToUnfcccVariables(units), FluxToUnfcccVariable.class))
				.onErrorMap(e -> new ServerException("Fluxes To UNFCCC Variable records creation failed", e));
	}
	
	private Flux<FluxToUnfcccVariable> createFluxesToUnfcccVariables(FluxToUnfcccVariable[] fluxToUnfcccVariables) {
		
		return
			repository
				.insertFluxesToUnfcccVariables(fluxToUnfcccVariables)
				.flatMap(id -> repository.selectFluxToUnfcccVariable(id));
			
	}



}
