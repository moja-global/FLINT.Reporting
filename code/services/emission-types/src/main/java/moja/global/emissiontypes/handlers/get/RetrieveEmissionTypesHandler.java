/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.handlers.get;

import moja.global.emissiontypes.exceptions.ServerException;
import moja.global.emissiontypes.models.EmissionType;
import moja.global.emissiontypes.repository.EmissionTypesRepository;
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
public class RetrieveEmissionTypesHandler {

	@Autowired
	EmissionTypesRepository repository;
		
	/**
	 * Retrieves all emission type records or specific emission type records if given their unique identifiers
	 * @param request the request, optionally containing the unique identifiers of the emission type records to be retrieved
	 * @return the stream of responses containing the details of the retrieved emission type records
	 */
	public Mono<ServerResponse> retrieveEmissionTypes(ServerRequest request) {

		log.trace("Entering retrieveEmissionTypes()");

		return
			Mono.just(getIds(request))
				.flatMap(ids ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(ids.length == 0 ? retrieveAllEmissionTypes() : retrieveEmissionTypesByIds(ids), EmissionType.class))
				.onErrorMap(e -> new ServerException("Emission Type records retrieval failed", e));
	}
	
	private Flux<EmissionType> retrieveAllEmissionTypes() {
		return 
			repository
				.selectAllEmissionTypes();
	}		

	private Flux<EmissionType> retrieveEmissionTypesByIds(Long[] ids) {
		
		return 
			repository
				.selectEmissionTypesByIds(ids);
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
