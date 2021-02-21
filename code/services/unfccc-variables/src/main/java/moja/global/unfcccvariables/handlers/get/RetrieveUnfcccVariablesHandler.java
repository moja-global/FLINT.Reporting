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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class RetrieveUnfcccVariablesHandler {

	@Autowired
	UnfcccVariablesRepository repository;
		
	/**
	 * Retrieves all UNFCCC variable records or specific UNFCCC variable records if given their unique identifiers
	 * @param request the request, optionally containing the unique identifiers of the UNFCCC variable records to be retrieved
	 * @return the stream of responses containing the details of the retrieved UNFCCC variable records
	 */
	public Mono<ServerResponse> retrieveUnfcccVariables(ServerRequest request) {

		log.trace("Entering retrieveUnfcccVariables()");

		return
			Mono.just(getIds(request))
				.flatMap(ids ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(ids.length == 0 ? retrieveAllUnfcccVariables() : retrieveUnfcccVariablesByIds(ids), UnfcccVariable.class))
				.onErrorMap(e -> new ServerException("UNFCCC Variable records retrieval failed", e));
	}
	
	private Flux<UnfcccVariable> retrieveAllUnfcccVariables() {
		return 
			repository
				.selectAllUnfcccVariables();
	}		

	private Flux<UnfcccVariable> retrieveUnfcccVariablesByIds(Long[] ids) {
		
		return 
			repository
				.selectUnfcccVariablesByIds(ids);
	}

	private Long[] getIds(ServerRequest request) {

		return
				request.queryParams().get("ids") == null ? new Long[]{}:
						request.queryParams()
								.get("ids")
								.stream()
								.map(Long::parseLong)
								.sorted()
								.toArray(Long[]::new);

	}

}
