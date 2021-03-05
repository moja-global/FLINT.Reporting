/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers.put;

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
public class UpdatePartyTypeHandler {

	@Autowired
    PartyTypesRepository repository;
	
	/**
	 * Updates a Party Type record
	 * @param request the request containing the details of the Party Type record to be updated
	 * @return the response containing the details of the newly updated Party Type record
	 */
	public Mono<ServerResponse> updatePartyType(ServerRequest request) {

		log.trace("Entering updatePartyType()");

		return 
			request
				.bodyToMono(PartyType.class)
					.flatMap(partyType ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updatePartyType(partyType), PartyType.class))
					.onErrorMap(e -> new ServerException("Party Type update failed", e));
	}
	
	
	private Mono<PartyType> updatePartyType(PartyType partyType){
		
		return 
			repository
				.updatePartyType(partyType)
				.flatMap(count -> repository.selectPartyType(partyType.getId()));
	}
}
