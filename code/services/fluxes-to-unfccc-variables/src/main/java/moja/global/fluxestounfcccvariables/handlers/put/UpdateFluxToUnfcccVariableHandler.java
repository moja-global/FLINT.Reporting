/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.handlers.put;

import moja.global.fluxestounfcccvariables.exceptions.ServerException;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.repository.FluxesToUnfcccVariablesRepository;
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
public class UpdateFluxToUnfcccVariableHandler {

	@Autowired
	FluxesToUnfcccVariablesRepository repository;
	
	/**
	 * Updates a flux to UNFCCC variable record
	 * @param request the request containing the details of the flux to UNFCCC variable record to be updated
	 * @return the response containing the details of the newly updated flux to UNFCCC variable record
	 */
	public Mono<ServerResponse> updateFluxToUnfcccVariable(ServerRequest request) {

		log.trace("Entering updateFluxToUnfcccVariable()");

		return 
			request
				.bodyToMono(FluxToUnfcccVariable.class)
					.flatMap(unit -> 
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateFluxToUnfcccVariable(unit), FluxToUnfcccVariable.class))
					.onErrorMap(e -> new ServerException("Flux To UNFCCC Variable record update failed", e));
	}
	
	
	private Mono<FluxToUnfcccVariable> updateFluxToUnfcccVariable(FluxToUnfcccVariable fluxToUnfcccVariable){
		
		return 
			repository
				.updateFluxToUnfcccVariable(fluxToUnfcccVariable)
				.flatMap(count -> repository.selectFluxToUnfcccVariable(fluxToUnfcccVariable.getId()));
	}
}
