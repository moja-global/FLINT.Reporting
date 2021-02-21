/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.handlers.put;

import moja.global.unfcccvariables.models.UnfcccVariable;
import moja.global.unfcccvariables.exceptions.ServerException;
import moja.global.unfcccvariables.repository.UnfcccVariablesRepository;
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
public class UpdateUnfcccVariablesHandler {

	@Autowired
	UnfcccVariablesRepository repository;
	
	/**
	 * Updates UNFCCC variable records
	 * @param request the request containing the details of the UNFCCC variable records to be updated
	 * @return the response containing the details of the newly updated UNFCCC variable records
	 */
	public Mono<ServerResponse> updateUnfcccVariables(ServerRequest request) {

		log.trace("Entering updateUnfcccVariables()");

		return 
			request
					.bodyToMono(UnfcccVariable[].class)
					.flatMap(units ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateUnfcccVariables(units), UnfcccVariable.class))
					.onErrorMap(e -> new ServerException("UNFCCC Variable records update failed", e));
	}
	
	private Flux<UnfcccVariable> updateUnfcccVariables(UnfcccVariable[] unfcccVariables) {
		return 
			Flux.fromStream(Arrays.stream(unfcccVariables).sorted())
				.flatMap(unit -> 
					repository
						.updateUnfcccVariable(unit)
						.flatMap(count -> repository.selectUnfcccVariableById(unit.getId())));

	}

	


}
