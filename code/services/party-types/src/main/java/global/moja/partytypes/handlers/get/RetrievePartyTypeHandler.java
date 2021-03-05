/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers.get;

import global.moja.partytypes.exceptions.ServerException;
import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.repository.PartyTypesRepository;
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
public class RetrievePartyTypeHandler {

	@Autowired
    PartyTypesRepository repository;

	/**
	 * Retrieves a Party Type record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Party Type record to be retrieved
	 * @return the response containing the details of the retrieved Party Type record
	 */
	public Mono<ServerResponse> retrievePartyType(ServerRequest request) {

		log.trace("Entering retrievePartyType()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrievePartyTypeById(Long.parseLong(request.pathVariable("id"))),
                        PartyType.class)
				.onErrorMap(e -> new ServerException("Party Type retrieval failed", e));
	}


	private Mono<PartyType> retrievePartyTypeById(Long id) {

		return
			repository
				.selectPartyType(id);
	}

}
