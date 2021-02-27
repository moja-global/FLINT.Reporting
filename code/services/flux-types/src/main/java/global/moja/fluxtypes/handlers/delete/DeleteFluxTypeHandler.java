/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.handlers.delete;

import global.moja.fluxtypes.exceptions.ServerException;
import global.moja.fluxtypes.repository.FluxTypesRepository;
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
public class DeleteFluxTypeHandler {

	@Autowired
    FluxTypesRepository repository;
	
	/**
	 * Deletes a Flux Type record
	 *
	 * @param request the request containing the details of the Flux Type record to be deleted
	 * @return the response containing the number of Flux Types records deleted
	 */
	public Mono<ServerResponse> deleteFluxType(ServerRequest request) {

		log.trace("Entering deleteFluxType()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteFluxTypeById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Flux Type deletion failed", e));

	}
	
	private Mono<Integer> deleteFluxTypeById(Long id){
		
		return 
			repository
				.deleteFluxTypeById(id);
	}

}
