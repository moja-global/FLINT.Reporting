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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreatePartiesHandler {

	@Autowired
    PartiesRepository repository;
	

	/**
	 * Recursively creates Parties records
	 *
	 * @param request the request containing the details of the Parties records to be created
	 * @return the stream of responses containing the details of the newly created Parties records
	 */
	public Mono<ServerResponse> createParties(ServerRequest request) {

		log.trace("Entering createParties()");

		return 
			request
				.bodyToMono(Party[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createParties(units), Party.class))
				.onErrorMap(e -> new ServerException("Parties creation failed", e));
	}
	
	private Flux<Party> createParties(Party[] parties) {
		
		return
			repository
				.insertParties(parties)
				.flatMap(id -> repository.selectParty(id));
			
	}



}
