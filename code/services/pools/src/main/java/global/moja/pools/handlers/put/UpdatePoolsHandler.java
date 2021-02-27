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
public class UpdatePoolsHandler {

	@Autowired
    PoolsRepository repository;
	
	/**
	 * Updates Pools records
	 * @param request the request containing the details of the Pools records to be updated
	 * @return the response containing the details of the newly updated Pools records
	 */
	public Mono<ServerResponse> updatePools(ServerRequest request) {

		log.trace("Entering updatePools()");

		return 
			request
					.bodyToMono(Pool[].class)
					.flatMap(pools->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updatePools(pools), Pool.class))
					.onErrorMap(e -> new ServerException("Pools update failed", e));
	}
	
	private Flux<Pool> updatePools(Pool[] pools) {
		return 
			Flux.fromStream(Arrays.stream(pools).sorted())
				.flatMap(unit -> 
					repository
						.updatePool(unit)
						.flatMap(count -> repository.selectPool(unit.getId())));

	}

	


}
