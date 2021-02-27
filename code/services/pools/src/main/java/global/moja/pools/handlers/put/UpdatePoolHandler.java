/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers.put;

import global.moja.pools.models.Pool;
import global.moja.pools.repository.PoolsRepository;
import global.moja.pools.exceptions.ServerException;
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
public class UpdatePoolHandler {

	@Autowired
    PoolsRepository repository;
	
	/**
	 * Updates a Pool record
	 * @param request the request containing the details of the Pool record to be updated
	 * @return the response containing the details of the newly updated Pool record
	 */
	public Mono<ServerResponse> updatePool(ServerRequest request) {

		log.trace("Entering updatePool()");

		return 
			request
				.bodyToMono(Pool.class)
					.flatMap(pool ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updatePool(pool), Pool.class))
					.onErrorMap(e -> new ServerException("Pool update failed", e));
	}
	
	
	private Mono<Pool> updatePool(Pool pool){
		
		return 
			repository
				.updatePool(pool)
				.flatMap(count -> repository.selectPool(pool.getId()));
	}
}
