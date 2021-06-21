/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.handlers.post;

import global.moja.units.models.Unit;
import global.moja.units.exceptions.ServerException;
import global.moja.units.repository.UnitsRepository;
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
public class CreateUnitHandler {

	@Autowired
	UnitsRepository repository;
	
	/**
	 * Creates a unit record
	 * @param request the request containing the details of the unit record to be created
	 * @return the response containing the details of the newly created unit record
	 */
	public Mono<ServerResponse> createUnit(ServerRequest request) {

		log.trace("Entering createUnit()");

		return 
			request
				.bodyToMono(Unit.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createUnit(unit),Unit.class))
				.onErrorMap(e -> new ServerException("Unit record creation failed", e));
	}
	
	
	private Mono<Unit> createUnit(Unit unit){
		
		return 
			repository
				.insertUnit(unit)
				.flatMap(id -> repository.selectUnitById(id));
		  
	}

}
