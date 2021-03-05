/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers.post;

import global.moja.partytypes.exceptions.ServerException;
import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.repository.PartyTypesRepository;
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
public class CreatePartyTypesHandler {

	@Autowired
    PartyTypesRepository repository;
	

	/**
	 * Recursively creates Party Types records
	 *
	 * @param request the request containing the details of the Party Types records to be created
	 * @return the stream of responses containing the details of the newly created Party Types records
	 */
	public Mono<ServerResponse> createPartyTypes(ServerRequest request) {

		log.trace("Entering createPartyTypes()");

		return 
			request
				.bodyToMono(PartyType[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createPartyTypes(units), PartyType.class))
				.onErrorMap(e -> new ServerException("Party Types creation failed", e));
	}
	
	private Flux<PartyType> createPartyTypes(PartyType[] partyTypes) {
		
		return
			repository
				.insertPartyTypes(partyTypes)
				.flatMap(id -> repository.selectPartyType(id));
			
	}



}
