/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.handlers.get;

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
public class RetrieveFluxToUnfcccVariableHandler {

	@Autowired
	FluxesToUnfcccVariablesRepository repository;

	/**
	 * Retrieves a flux to UNFCCC variable record given its unique identifier
	 * @param request the request containing the unique identifier of the flux to UNFCCC variable record to be retrieved
	 * @return the response containing the details of the retrieved flux to UNFCCC variable record
	 */
	public Mono<ServerResponse> retrieveFluxToUnfcccVariable(ServerRequest request) {

		log.trace("Entering retrieveFluxToUnfcccVariable()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveUnfcccVariableById(Long.parseLong(request.pathVariable("id"))),
                        FluxToUnfcccVariable.class)
				.onErrorMap(e -> new ServerException("Flux To UNFCCC Variable record retrieval failed", e));
	}


	private Mono<FluxToUnfcccVariable> retrieveUnfcccVariableById(Long id) {

		return
			repository
				.selectFluxToUnfcccVariable(id);
	}

}
