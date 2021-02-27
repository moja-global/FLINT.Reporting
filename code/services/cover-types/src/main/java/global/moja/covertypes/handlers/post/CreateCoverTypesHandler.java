/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.handlers.post;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.exceptions.ServerException;
import global.moja.covertypes.repository.CoverTypesRepository;
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
public class CreateCoverTypesHandler {

	@Autowired
    CoverTypesRepository repository;
	

	/**
	 * Recursively creates Cover Types records
	 *
	 * @param request the request containing the details of the Cover Types records to be created
	 * @return the stream of responses containing the details of the newly created Cover Types records
	 */
	public Mono<ServerResponse> createCoverTypes(ServerRequest request) {

		log.trace("Entering createCoverTypes()");

		return 
			request
				.bodyToMono(CoverType[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createCoverTypes(units), CoverType.class))
				.onErrorMap(e -> new ServerException("Cover Types creation failed", e));
	}
	
	private Flux<CoverType> createCoverTypes(CoverType[] coverTypes) {
		
		return
			repository
				.insertCoverTypes(coverTypes)
				.flatMap(id -> repository.selectCoverType(id));
			
	}



}
