/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers.delete;

import global.moja.partytypes.exceptions.ServerException;
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
public class DeletePartyTypeHandler {

	@Autowired
	PartyTypesRepository repository;
	
	/**
	 * Deletes a Party Type record
	 *
	 * @param request the request containing the details of the Party Type record to be deleted
	 * @return the response containing the number of Party Types records deleted
	 */
	public Mono<ServerResponse> deletePartyType(ServerRequest request) {

		log.trace("Entering deletePartyType()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deletePartyTypeById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Party Type deletion failed", e));

	}
	
	private Mono<Integer> deletePartyTypeById(Long id){
		
		return 
			repository
				.deletePartyTypeById(id);
	}

}
