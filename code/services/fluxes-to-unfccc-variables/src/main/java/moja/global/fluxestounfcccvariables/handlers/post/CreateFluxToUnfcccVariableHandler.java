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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateFluxToUnfcccVariableHandler {

	@Autowired
	FluxesToUnfcccVariablesRepository repository;
	
	/**
	 * Creates a flux to UNFCCC variable record
	 * @param request the request containing the details of the flux to UNFCCC variable record to be created
	 * @return the response containing the details of the newly created flux to UNFCCC variable record
	 */
	public Mono<ServerResponse> createFluxToUnfcccVariable(ServerRequest request) {

		log.trace("Entering createFluxToUnfcccVariable()");

		return 
			request
				.bodyToMono(FluxToUnfcccVariable.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createFluxToUnfcccVariable(unit), FluxToUnfcccVariable.class))
				.onErrorMap(e -> new ServerException("Flux To UNFCCC Variable record creation failed", e));
	}
	
	
	private Mono<FluxToUnfcccVariable> createFluxToUnfcccVariable(FluxToUnfcccVariable fluxToUnfcccVariable){
		
		return 
			repository
				.insertFluxToUnfcccVariable(fluxToUnfcccVariable)
				.flatMap(id -> repository.selectFluxToUnfcccVariable(id));
		  
	}

}
