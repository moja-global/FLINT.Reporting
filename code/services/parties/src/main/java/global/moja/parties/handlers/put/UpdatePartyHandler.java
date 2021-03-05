/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.handlers.put;

import global.moja.parties.exceptions.ServerException;
import global.moja.parties.models.Party;
import global.moja.parties.repository.PartiesRepository;
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
public class UpdatePartyHandler {

	@Autowired
    PartiesRepository repository;
	
	/**
	 * Updates a Party record
	 * @param request the request containing the details of the Party record to be updated
	 * @return the response containing the details of the newly updated Party record
	 */
	public Mono<ServerResponse> updateParty(ServerRequest request) {

		log.trace("Entering updateParty()");

		return 
			request
				.bodyToMono(Party.class)
					.flatMap(party ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateParty(party), Party.class))
					.onErrorMap(e -> new ServerException("Party update failed", e));
	}
	
	
	private Mono<Party> updateParty(Party party){
		
		return 
			repository
				.updateParty(party)
				.flatMap(count -> repository.selectParty(party.getId()));
	}
}
