/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.handlers.get;

import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
import global.moja.fluxestoreportingvariables.exceptions.ServerException;
import global.moja.fluxestoreportingvariables.repository.FluxesToReportingVariablesRepository;
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
public class RetrieveFluxToReportingVariableHandler {

	@Autowired
	FluxesToReportingVariablesRepository repository;

	/**
	 * Retrieves a flux to reporting variable record given its unique identifier
	 * @param request the request containing the unique identifier of the flux to reporting variable record to be retrieved
	 * @return the response containing the details of the retrieved flux to reporting variable record
	 */
	public Mono<ServerResponse> retrieveFluxToReportingVariable(ServerRequest request) {

		log.trace("Entering retrieveFluxToReportingVariable()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveUnfcccVariableById(Long.parseLong(request.pathVariable("id"))),
                        FluxToReportingVariable.class)
				.onErrorMap(e -> new ServerException("Flux To Reporting Variable record retrieval failed", e));
	}


	private Mono<FluxToReportingVariable> retrieveUnfcccVariableById(Long id) {

		return
			repository
				.selectFluxToReportingVariable(id);
	}

}
