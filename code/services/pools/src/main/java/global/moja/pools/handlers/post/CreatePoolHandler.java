/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers.post;

import global.moja.pools.models.Pool;
import global.moja.pools.repository.PoolsRepository;
import global.moja.pools.exceptions.ServerException;
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
public class CreatePoolHandler {

	@Autowired
    PoolsRepository repository;
	
	/**
	 * Creates a Pool record
	 *
	 * @param request the request containing the details of the Pool record to be created
	 * @return the response containing the details of the newly created Pool record
	 */
	public Mono<ServerResponse> createPool(ServerRequest request) {

		log.trace("Entering createPool()");

		return 
			request
				.bodyToMono(Pool.class)
				.flatMap(pool ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createPool(pool), Pool.class))
				.onErrorMap(e -> new ServerException("Pool creation failed", e));
	}
	
	
	private Mono<Pool> createPool(Pool pool){
		
		return 
			repository
				.insertPool(pool)
				.flatMap(id -> repository.selectPool(id));
		  
	}

}
