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
public class UpdatePartyTypesHandler {

	@Autowired
    PartyTypesRepository repository;
	
	/**
	 * Updates Party Types records
	 * @param request the request containing the details of the Party Types records to be updated
	 * @return the response containing the details of the newly updated Party Types records
	 */
	public Mono<ServerResponse> updatePartyTypes(ServerRequest request) {

		log.trace("Entering updatePartyTypes()");

		return 
			request
					.bodyToMono(PartyType[].class)
					.flatMap(partyTypes->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updatePartyTypes(partyTypes), PartyType.class))
					.onErrorMap(e -> new ServerException("Party Types update failed", e));
	}
	
	private Flux<PartyType> updatePartyTypes(PartyType[] partyTypes) {
		return 
			Flux.fromStream(Arrays.stream(partyTypes).sorted())
				.flatMap(unit -> 
					repository
						.updatePartyType(unit)
						.flatMap(count -> repository.selectPartyType(unit.getId())));

	}

	


}
