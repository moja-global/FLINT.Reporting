/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers.post;

import global.moja.pools.repository.PoolsRepository;
import global.moja.pools.exceptions.ServerException;
import global.moja.pools.models.Pool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreatePoolsHandler {

	@Autowired
    PoolsRepository repository;
	

	/**
	 * Recursively creates Pools records
	 *
	 * @param request the request containing the details of the Pools records to be created
	 * @return the stream of responses containing the details of the newly created Pools records
	 */
	public Mono<ServerResponse> createPools(ServerRequest request) {

		log.trace("Entering createPools()");

		return 
			request
				.bodyToMono(Pool[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createPools(units), Pool.class))
				.onErrorMap(e -> new ServerException("Pools creation failed", e));
	}
	
	private Flux<Pool> createPools(Pool[] pools) {
		
		return
			repository
				.insertPools(pools)
				.flatMap(id -> repository.selectPool(id));
			
	}



}
