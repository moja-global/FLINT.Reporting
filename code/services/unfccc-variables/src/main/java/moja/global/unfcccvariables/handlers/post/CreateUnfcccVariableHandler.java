/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.handlers.post;

import moja.global.unfcccvariables.exceptions.ServerException;
import moja.global.unfcccvariables.models.UnfcccVariable;
import moja.global.unfcccvariables.repository.UnfcccVariablesRepository;
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
public class CreateUnfcccVariableHandler {

	@Autowired
	UnfcccVariablesRepository repository;
	
	/**
	 * Creates a UNFCCC variable record
	 * @param request the request containing the details of the UNFCCC variable record to be created
	 * @return the response containing the details of the newly created UNFCCC variable record
	 */
	public Mono<ServerResponse> createUnfcccVariable(ServerRequest request) {

		log.trace("Entering createUnfcccVariable()");

		return 
			request
				.bodyToMono(UnfcccVariable.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createUnfcccVariable(unit), UnfcccVariable.class))
				.onErrorMap(e -> new ServerException("UNFCCC Variable record creation failed", e));
	}
	
	
	private Mono<UnfcccVariable> createUnfcccVariable(UnfcccVariable unfcccVariable){
		
		return 
			repository
				.insertUnfcccVariable(unfcccVariable)
				.flatMap(id -> repository.selectUnfcccVariableById(id));
		  
	}

}
