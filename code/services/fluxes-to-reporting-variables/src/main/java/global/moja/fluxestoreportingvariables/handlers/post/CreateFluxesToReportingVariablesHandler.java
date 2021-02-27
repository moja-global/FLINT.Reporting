/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.handlers.post;

import global.moja.fluxestoreportingvariables.exceptions.ServerException;
import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
import global.moja.fluxestoreportingvariables.repository.FluxesToReportingVariablesRepository;
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
public class CreateFluxesToReportingVariablesHandler {

	@Autowired
	FluxesToReportingVariablesRepository repository;
	

	/**
	 * Recursively creates fluxes to reporting variables records
	 * @param request the request containing the details of the fluxes to reporting variable records to be created
	 * @return the stream of responses containing the details of the newly created fluxes to reporting variable records
	 */
	public Mono<ServerResponse> createFluxesToReportingVariables(ServerRequest request) {

		log.trace("Entering createFluxesToReportingVariables()");

		return 
			request
				.bodyToMono(FluxToReportingVariable[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createFluxesToReportingVariables(units), FluxToReportingVariable.class))
				.onErrorMap(e -> new ServerException("Fluxes To Reporting Variable records creation failed", e));
	}
	
	private Flux<FluxToReportingVariable> createFluxesToReportingVariables(FluxToReportingVariable[] fluxToReportingVariables) {
		
		return
			repository
				.insertFluxesToReportingVariables(fluxToReportingVariables)
				.flatMap(id -> repository.selectFluxToReportingVariable(id));
			
	}



}
