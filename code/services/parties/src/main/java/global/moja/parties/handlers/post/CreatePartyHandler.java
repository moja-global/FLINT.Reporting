/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.handlers.post;

import global.moja.parties.exceptions.ServerException;
import global.moja.parties.models.Party;
import global.moja.parties.repository.PartiesRepository;
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
public class CreatePartyHandler {

	@Autowired
    PartiesRepository repository;
	
	/**
	 * Creates a Party record
	 *
	 * @param request the request containing the details of the Party record to be created
	 * @return the response containing the details of the newly created Party record
	 */
	public Mono<ServerResponse> createParty(ServerRequest request) {

		log.trace("Entering createParty()");

		return 
			request
				.bodyToMono(Party.class)
				.flatMap(party ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createParty(party), Party.class))
				.onErrorMap(e -> new ServerException("Party creation failed", e));
	}
	
	
	private Mono<Party> createParty(Party party){
		
		return 
			repository
				.insertParty(party)
				.flatMap(id -> repository.selectParty(id));
		  
	}

}
