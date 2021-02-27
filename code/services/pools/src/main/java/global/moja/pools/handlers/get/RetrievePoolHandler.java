/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers.get;

import global.moja.pools.exceptions.ServerException;
import global.moja.pools.models.Pool;
import global.moja.pools.repository.PoolsRepository;
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
public class RetrievePoolHandler {

	@Autowired
    PoolsRepository repository;

	/**
	 * Retrieves a Pool record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Pool record to be retrieved
	 * @return the response containing the details of the retrieved Pool record
	 */
	public Mono<ServerResponse> retrievePool(ServerRequest request) {

		log.trace("Entering retrievePool()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrievePoolById(Long.parseLong(request.pathVariable("id"))),
                        Pool.class)
				.onErrorMap(e -> new ServerException("Pool retrieval failed", e));
	}


	private Mono<Pool> retrievePoolById(Long id) {

		return
			repository
				.selectPool(id);
	}

}
