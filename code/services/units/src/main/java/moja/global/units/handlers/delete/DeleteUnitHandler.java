/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.units.handlers.delete;

import moja.global.units.exceptions.ServerException;
import moja.global.units.repository.UnitsRepository;
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
public class DeleteUnitHandler {

	@Autowired
	UnitsRepository repository;
	
	/**
	 * Deletes a unit record
	 * @param request the request containing the details of the unit record to be created
	 * @return the response containing the number of records deleted
	 */
	public Mono<ServerResponse> deleteUnit(ServerRequest request) {

		log.trace("Entering deleteUnit()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteUnitById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Unit record deletion failed", e));

	}
	
	private Mono<Integer> deleteUnitById(Long id){
		
		return 
			repository
				.deleteUnitById(id);
	}

}
