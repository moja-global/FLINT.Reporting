/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.handlers.get;

import moja.global.unfcccvariables.exceptions.ServerException;
import moja.global.unfcccvariables.models.UnfcccVariable;
import moja.global.unfcccvariables.repository.UnfcccVariablesRepository;
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
public class RetrieveUnfcccVariableHandler {

	@Autowired
	UnfcccVariablesRepository repository;
	
	/**
	 * Retrieves a UNFCCC variable record given its unique identifier
	 * @param request the request containing the unique identifier of the UNFCCC variable record to be retrieved
	 * @return the response containing the details of the retrieved UNFCCC variable record
	 */
	public Mono<ServerResponse> retrieveUnfcccVariable(ServerRequest request) {

		log.trace("Entering retrieveUnfcccVariableById()");

		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveUnfcccVariableById(Long.parseLong(request.pathVariable("id"))), UnfcccVariable.class)
				.onErrorMap(e -> new ServerException("UNFCCC Variable record retrieval failed", e));
	}
	
	
	private Mono<UnfcccVariable> retrieveUnfcccVariableById(Long id) {
		
		return 
			repository
				.selectUnfcccVariableById(id);
	}

}
