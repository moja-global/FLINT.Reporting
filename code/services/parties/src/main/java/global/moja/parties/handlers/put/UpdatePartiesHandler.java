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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdatePartiesHandler {

	@Autowired
	PartiesRepository repository;
	
	/**
	 * Updates Parties records
	 * @param request the request containing the details of the Parties records to be updated
	 * @return the response containing the details of the newly updated Parties records
	 */
	public Mono<ServerResponse> updateParties(ServerRequest request) {

		log.trace("Entering updateParties()");

		return 
			request
					.bodyToMono(Party[].class)
					.flatMap(parties->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateParties(parties), Party.class))
					.onErrorMap(e -> new ServerException("Parties update failed", e));
	}
	
	private Flux<Party> updateParties(Party[] parties) {
		return 
			Flux.fromStream(Arrays.stream(parties).sorted())
				.flatMap(unit -> 
					repository
						.updateParty(unit)
						.flatMap(count -> repository.selectParty(unit.getId())));

	}

	


}
