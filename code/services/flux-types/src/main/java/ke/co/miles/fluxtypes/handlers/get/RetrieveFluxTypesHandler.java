/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.handlers.get;

import ke.co.miles.fluxtypes.exceptions.ServerException;
import ke.co.miles.fluxtypes.models.FluxType;
import ke.co.miles.fluxtypes.repository.FluxTypesRepository;
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
public class RetrieveFluxTypesHandler {

	@Autowired
	FluxTypesRepository repository;
		
	/**
	 * Retrieves all flux type records or specific flux type records if given their unique identifiers
	 * @param request the request, optionally containing the unique identifiers of the flux type records to be retrieved
	 * @return the stream of responses containing the details of the retrieved flux type records
	 */
	public Mono<ServerResponse> retrieveFluxTypes(ServerRequest request) {

		log.trace("Entering retrieveFluxTypes()");

		return
			Mono.just(getIds(request))
				.flatMap(ids ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(ids.length == 0 ? retrieveAllFluxTypes() : retrieveFluxTypesByIds(ids), FluxType.class))
				.onErrorMap(e -> new ServerException("Flux Type records retrieval failed", e));
	}
	
	private Flux<FluxType> retrieveAllFluxTypes() {
		return 
			repository
				.selectAllFluxTypes();
	}		

	private Flux<FluxType> retrieveFluxTypesByIds(Long[] ids) {
		
		return 
			repository
				.selectFluxTypesByIds(ids);
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
