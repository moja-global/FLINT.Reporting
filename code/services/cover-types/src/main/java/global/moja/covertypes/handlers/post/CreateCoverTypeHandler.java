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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateCoverTypeHandler {

	@Autowired
    CoverTypesRepository repository;
	
	/**
	 * Creates a Cover Type record
	 *
	 * @param request the request containing the details of the Cover Type record to be created
	 * @return the response containing the details of the newly created Cover Type record
	 */
	public Mono<ServerResponse> createCoverType(ServerRequest request) {

		log.trace("Entering createCoverType()");

		return 
			request
				.bodyToMono(CoverType.class)
				.flatMap(coverType ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createCoverType(coverType), CoverType.class))
				.onErrorMap(e -> new ServerException("Cover Type creation failed", e));
	}
	
	
	private Mono<CoverType> createCoverType(CoverType coverType){
		
		return 
			repository
				.insertCoverType(coverType)
				.flatMap(id -> repository.selectCoverType(id));
		  
	}

}
