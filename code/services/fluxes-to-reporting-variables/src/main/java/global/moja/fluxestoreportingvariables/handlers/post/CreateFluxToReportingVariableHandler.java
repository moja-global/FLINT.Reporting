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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateFluxToReportingVariableHandler {

	@Autowired
	FluxesToReportingVariablesRepository repository;
	
	/**
	 * Creates a flux to reporting variable record
	 * @param request the request containing the details of the flux to reporting variable record to be created
	 * @return the response containing the details of the newly created flux to reporting variable record
	 */
	public Mono<ServerResponse> createFluxToReportingVariable(ServerRequest request) {

		log.trace("Entering createFluxToReportingVariable()");

		return 
			request
				.bodyToMono(FluxToReportingVariable.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createFluxToReportingVariable(unit), FluxToReportingVariable.class))
				.onErrorMap(e -> new ServerException("Flux To Reporting Variable record creation failed", e));
	}
	
	
	private Mono<FluxToReportingVariable> createFluxToReportingVariable(FluxToReportingVariable fluxToReportingVariable){
		
		return 
			repository
				.insertFluxToReportingVariable(fluxToReportingVariable)
				.flatMap(id -> repository.selectFluxToReportingVariable(id));
		  
	}

}
