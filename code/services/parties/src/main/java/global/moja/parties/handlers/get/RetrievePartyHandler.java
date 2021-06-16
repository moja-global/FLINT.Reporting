/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.handlers.get;

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
public class RetrievePartyHandler {

	@Autowired
    PartiesRepository repository;

	/**
	 * Retrieves a Party record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Party record to be retrieved
	 * @return the response containing the details of the retrieved Party record
	 */
	public Mono<ServerResponse> retrieveParty(ServerRequest request) {

		log.trace("Entering retrieveParty()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrievePartyById(Long.parseLong(request.pathVariable("id"))),Party.class)
				.onErrorMap(e -> new ServerException("Party retrieval failed", e));
	}


	private Mono<Party> retrievePartyById(Long id) {

		return
			repository
				.selectParty(id);
	}

}
